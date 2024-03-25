package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@GetMapping("/")
	public String getHome(Model carRegisterList, Model carsInHcm, Model carsInHn, Model carsInDn, Model carsInBd) {
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
		carsInHcm.addAttribute("carsInHcm", listcarsInHcm);
		carsInHn.addAttribute("carsInHn", listcarsInHn);
		carsInDn.addAttribute("carsInDn", listcarsInDn);
		carsInBd.addAttribute("carsInBd", listcarsInBd);
		listDto.removeIf(car -> car.getDiscount() == 0);
		carRegisterList.addAttribute("carRegisterList", listDto);
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
		return "/authentications/register";
	}

	@PostMapping("/register")
	public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
		Users existingUser = uServices.findUserByEmail(userDto.getEmail());

		if (existingUser != null) {
		    result.rejectValue("email", null, "Email already registered !!!");
		}

		if (!userDto.isPasswordMatching()) {
			result.rejectValue("confirmPassword", null, "Confirm Password do not match.");
			return "/authentications/register";
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "/authentications/register";
		}

		
		try {
			uServices.saveUser(userDto);
			try {
				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("recipientName", userDto.getEmail());
				templateModel.put("hello", "Welcome to AzCar");
				templateModel.put("text", "Cảm ơn Bạn đã sử dụng dịch vụ thuê xe của chúng tôi");
				emailService.sendMessageUsingThymeleafTemplate(userDto.getEmail(), "AzCar", templateModel);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return "authentications/login";
		}catch (Exception e) {
			System.out.println(e);
		}
		
		return "authentications/register";
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
		}
		return "/user/profile/{email}" + email;

	}

	@PostMapping("/user/profile/edit/{email}")
	public String editProfile(@PathVariable("email") String email, @ModelAttribute("uDto") UserDto uDto, Model model) {

		uServices.editProfile(email, uDto);

		return "redirect:/user/profile/{email}";

	}

	@GetMapping("/user/profile/changePassword/{email}")
	public String changePasswordForm(@PathVariable("email") String email, Model model) {
		Users user = uServices.findUserByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("password", "");
		return "/authentications/changePassword";
	}

	@PostMapping("/user/profile/changePassword/{email}")
	public String changePassword(@PathVariable("email") String email, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		Users user = uServices.findUserByEmail(request.getSession().getAttribute("emailLogin").toString());

		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
			return "redirect:/user/profile/changePassword/{email}";
		}

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			redirectAttributes.addFlashAttribute("error", "OldPasswords do not match.");
			return "redirect:/user/profile/changePassword/{email}";
		}
		try {
			user.setPassword(passwordEncoder.encode(newPassword));
			uServices.saveUserReset(user);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/user/profile/{email}";

	}

	@GetMapping("/forgot_password")
	public String showForgotPassword(Model model) {
		model.addAttribute("email", "Forgot Password");
		return "/authentications/forgot_password";
	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model, UserDto userForgot) {
		Users user = uServices.findUserByEmail(userForgot.getEmail());
		String email = request.getParameter("email");
		String token = RandomString.make(45);
		System.out.println("Email: " + email);
		System.out.println("token: " + token);
		if (user != null) {
			try {

				uServices.updateResetPasswordToken(token, email);
				String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password/token/" + token;
				sendEmail(email, resetPasswordLink);
				model.addAttribute("success", "We have sent a reset password link to your email.");

			} catch (Exception e) {
				model.addAttribute("errors", "Email not emty!!");
			}
		} else {
			model.addAttribute("error", "Email Wrong, Please check your Email!!");
		}

		model.addAttribute("email", "Forgot Password");
		return "/authentications/forgot_password";
	}

	@GetMapping("/reset_password/token/{token}")
	public String showResetPasswordForm(@PathVariable("token") String token, Model user, Model tokenModel) {
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
	public String processResetPassword(Model user, @ModelAttribute(name = "password") String password,
			@ModelAttribute(name = "tokenModel") String tokenModel) {

		var codeModel = uServices.findUserByToken(tokenModel);

		if (codeModel != null) {
			codeModel.setPassword(passwordEncoder.encode(password));
			uServices.saveUserReset(codeModel);
			return "/authentications/login";
		} else {
			return "/authentications/reset_password?error";
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
				return "redirect:/user/profile/edit/uploadFile" + "?succesUpdateImage";
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return "redirect:/user/profile/edit/uploadFile" + "?failUpdateImage";

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
				+ "<p>Ignore this email if you do remember your password, or you have not made the request</p>"
				+ "<p>Chào mừng bạn đến với dịch vụ AzCar</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}


}
