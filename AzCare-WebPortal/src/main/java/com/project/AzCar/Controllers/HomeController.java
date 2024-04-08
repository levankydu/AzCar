package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Mailer.EmailService;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;
import com.project.AzCar.Utilities.Utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;
import net.minidev.json.JSONObject;

@Controller
public class HomeController {

	@Autowired
	private UserServices uServices;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CarServices carServices;
	@Autowired
	private ProvinceServices provinceServices;
	@Autowired
	private BrandServices brandServices;
	@Autowired
	private CarImageServices carImageServices;

	@Autowired
	private FilesStorageServices fileStorageServices;
	@Autowired
	private UserServices userServices;

	@GetMapping("/")
	public String getHome(Model ModelView, HttpServletRequest request) {
		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> listDto = new ArrayList<>();
		List<CarInforDto> listcarsInHcm = new ArrayList<>();
		List<CarInforDto> listcarsInHn = new ArrayList<>();
		List<CarInforDto> listcarsInDn = new ArrayList<>();
		List<CarInforDto> listcarsInBd = new ArrayList<>();
		List<String> listProvince = provinceServices.getListCityString();
		for (var item : list) {
			var itemDto = carServices.mapToDto(item.getId());
			itemDto.setCarmodel(brandServices.getModel(item.getModelId()));
			itemDto.setImages(carImageServices.getImgByCarId(item.getId()));
			for (var c : listProvince) {
				if (item.getAddress().contains(c)) {
					itemDto.setAddress(c);
				}
			}

			listDto.add(itemDto);
		}
		listDto.removeIf(car -> !car.getStatus().equals(Constants.carStatus.READY));
		if (request.getSession().getAttribute("emailLogin") != null) {
			String email = request.getSession().getAttribute("emailLogin").toString();
			Users owner = userServices.findUserByEmail(email);
			listDto.removeIf(car -> car.getCarOwnerId() == (int) owner.getId());
		}

		for (var item : listDto) {
			if (item.getAddress().contains("Hồ Chí Minh")) {
				listcarsInHcm.add(item);
			}
		}
		for (var item : listDto) {
			if (item.getAddress().contains("Hà Nội")) {
				listcarsInHn.add(item);
			}
		}
		for (var item : listDto) {
			if (item.getAddress().contains("Đà Nẵng")) {
				listcarsInDn.add(item);
			}
		}
		for (var item : listDto) {
			if (item.getAddress().contains("Bình Dương")) {
				listcarsInBd.add(item);
			}
		}
		ModelView.addAttribute("carsInHcm", listcarsInHcm);
		ModelView.addAttribute("carsInHn", listcarsInHn);
		ModelView.addAttribute("carsInDn", listcarsInDn);
		ModelView.addAttribute("carsInBd", listcarsInBd);
		listDto.removeIf(car -> car.getDiscount() == 0);

		ModelView.addAttribute("carRegisterList", listDto);
		return "index";
	}

	@GetMapping("/get/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) throws IOException {
		List<CarInfor> list = carServices.findAll();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/carImages/" + list.get(i).getModelId() + "-" + list.get(i).getId();
			Resource fileResource = fileStorageServices.load(filename, dir);
			if (fileResource == null) {
				i++;

			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileResource.getFilename() + "\"").body(fileResource);
			}

		}
		return null;

	}

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("user", new Users());
		return "/authentications/login";
	}

	@GetMapping("/register")
	public String registrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "authentications/register";
	}

	@PostMapping("/register")
	public ResponseEntity<?> registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result) {
		Users existingUser = uServices.findUserByEmail(userDto.getEmail());

		if (existingUser != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered !!!");
		}
		try {
			uServices.saveUser(userDto);
			try {
				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("recipientName", userDto.getEmail());
				templateModel.put("hello", "Welcome to AzCar");
				templateModel.put("text", "Cảm ơn Bạn đã sử dụng dịch vụ thuê xe của chúng tôi");
				emailService.sendMessageUsingThymeleafTemplate(userDto.getEmail(), "AzCar", templateModel);
				return ResponseEntity.ok("Registration successful.");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred.");
		}
	}

	@GetMapping(path = "/registeradmin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<JSONObject> registrationAdmin(@ModelAttribute UserDto userDto) {

		if (uServices.saveAdmin(new UserDto())) {
			JSONObject entity = new JSONObject();
			entity.put("Code 1", "Created Admin Account");

			return ResponseEntity.ok(entity);
		} else {
			JSONObject entity = new JSONObject();
			entity.put("Code 0", "Created Admin Account Fail, Addmin Account already created");

			return ResponseEntity.ok(entity);
		}

	}

	@GetMapping("/user/profile/{email}")
	public String profile(@PathVariable("email") String email, Model model, Model dirImage) {

		Users user = uServices.findUserByEmail(email);

		model.addAttribute("user", user);

		return "/authentications/profile";
	}

	@GetMapping("/user/profile/edit/{email}")
	public String editProfile(@PathVariable("email") String email, Model model) {
		Users user = uServices.findUserByEmail(email);
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("uDto", new UserDto());
			model.addAttribute("uDto", new Users());

			return "authentications/edit";
		} else {

			return "redirect:/user/profile/" + email;
		}
	}

	@PostMapping("/user/profile/edit/{email}")
	@ResponseBody
	public ResponseEntity<String> editProfile(@PathVariable("email") String email,
			@ModelAttribute("uDto") UserDto uDto) {
		try {
			uServices.editProfile(email, uDto);
			return ResponseEntity.ok("Profile updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating profile: " + e.getMessage());
		}
	}

	@GetMapping("/user/profile/changePassword/{email}")
	public String changePasswordForm(@PathVariable("email") String email, Model model) {
		Users user = uServices.findUserByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("password", "");
		return "authentications/changePassword";
	}

	@PostMapping("/user/profile/changePassword/{email}")
	public ResponseEntity<?> changePassword(@PathVariable("email") String email,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, HttpServletRequest request) {

		Users user = uServices.findUserByEmail(request.getSession().getAttribute("emailLogin").toString());

		String dbPassword = user.getPassword();

		if (!newPassword.equals(confirmPassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
		}

		if (!passwordEncoder.matches(oldPassword, dbPassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
		}

		try {

			if (passwordEncoder.matches(newPassword, dbPassword)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("New password cannot be the same as old password.");
			}

			user.setPassword(passwordEncoder.encode(newPassword));
			uServices.saveUserReset(user);
			return ResponseEntity.ok("Password changed successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password.");
		}
	}

	private Set<String> usedTokens = new HashSet<>();

	@GetMapping("/forgot_password")
	public String showForgotPassword(Model model) {
		model.addAttribute("email", "Forgot Password");
		return "/authentications/forgot_password";
	}

	@PostMapping("/forgot_password")
	public ResponseEntity<String> processForgotPassword(@ModelAttribute("userForgot") UserDto userForgot,
			HttpServletRequest request) {
		String email = userForgot.getEmail();
		if (email == null || email.isEmpty()) {
			return ResponseEntity.badRequest().body("Email is required.");
		}

		Users user = uServices.findUserByEmail(email);
		if (user != null) {
			String token = RandomString.make(10);
			System.out.println("Email: " + email);
			System.out.println("Token: " + token);
			try {
				uServices.updateResetPasswordToken(token, email);
				String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password/token/" + token;
				sendEmail(email, resetPasswordLink);
				return ResponseEntity.ok("We have sent a reset password link to your email.");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failed to send reset password link. Please try again later.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Email not found. Please check your email address.");
		}
	}

	@GetMapping("/reset_password/token/{token}")
	public String showResetPasswordForm(@PathVariable("token") String token, Model user, Model tokenModel) {

		if (usedTokens.contains(token)) {

			return "authentications/tokenErrorPage";
		}

		var codeModel = uServices.findUserByToken(token);
		if (codeModel != null) {
			user.addAttribute("user", codeModel);
			tokenModel.addAttribute("tokenModel", token);
			return "/authentications/reset_password";
		} else {
			return "redirect:/authentications/login";
		}
	}

	@PostMapping("/reset_password/token")
	public ResponseEntity<String> processResetPassword(@ModelAttribute("password") String password,
			@ModelAttribute("tokenModel") String tokenModel) {
		if (usedTokens.contains(tokenModel)) {
			return ResponseEntity.badRequest().body("Token has already been used.");
		}

		var codeModel = uServices.findUserByToken(tokenModel);
		if (codeModel != null) {
			codeModel.setPassword(passwordEncoder.encode(password));
			uServices.saveUserReset(codeModel);
			usedTokens.add(tokenModel); // Đánh dấu token đã được sử dụng
			return ResponseEntity.ok("Password changed successfully.");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/user/profile/avatar/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename, HttpServletRequest request) {
		Users user = uServices.findUserByEmail(request.getSession().getAttribute("emailLogin").toString());

		String dir = Constants.ImgDir.USER_DIR + "/" + user.getEmail().replace(".", "-");

		Resource file = fileStorageServices.load(filename, dir);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/user/profile/edit/avatar/{filename}")
	public ResponseEntity<Resource> getImageEdit(@PathVariable("filename") String filename,
			HttpServletRequest request) {
		Users user = uServices.findUserByEmail(request.getSession().getAttribute("emailLogin").toString());

		String dir = Constants.ImgDir.USER_DIR + "/" + user.getEmail().replace(".", "-");

		Resource file = fileStorageServices.load(filename, dir);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/user/profile/flutter/avatar/{filename}")
	public ResponseEntity<Resource> getImageToFlutter(@PathVariable("filename") String filename) throws IOException {
		List<Users> list = uServices.findAllUsers();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/userImages/" + list.get(i).getEmail().replace(".", "-");
			Resource fileResource = fileStorageServices.load(filename, dir);
			if (fileResource == null) {
				i++;

			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileResource.getFilename() + "\"").body(fileResource);
			}

		}
		return null;

	}

	@PostMapping("/user/profile/edit/uploadFile")
	public String uploadImage(@RequestParam(name = "image") MultipartFile image, HttpServletRequest request) {

		Users user = uServices.findUserByEmail(request.getSession().getAttribute("emailLogin").toString());

		if (user != null) {
			String dir = Constants.ImgDir.USER_DIR + "/" + user.getEmail().replace(".", "-");
			Path path = Paths.get(dir);

			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new RuntimeException("Error create Folder!!!!");
			}

			try {
				fileStorageServices.save(image, dir);
				user.setImage(image.getOriginalFilename());
				uServices.saveUserReset(user);
				return "redirect:/user/profile/" + user.getEmail();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return "redirect:/user/profile/" + user.getEmail();
	}

	private void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Here's the link to reset your password";
		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password.</p>" + "<p><b><a href=\"" + resetPasswordLink
				+ "\" >Change my Password</a></b></p>"
				+ "<p>Ignore this email if you do remember your password, or you have not made the request</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

}
