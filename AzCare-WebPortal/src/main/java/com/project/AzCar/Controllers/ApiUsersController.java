package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.CarInfos.CarInforFlutter;
import com.project.AzCar.Dto.DriverLicense.DriverLicenseBack;
import com.project.AzCar.Dto.DriverLicense.DriverLicenseFront;
import com.project.AzCar.Dto.Users.EditApiDto;
import com.project.AzCar.Dto.Users.ForgotPasswordApiDto;
import com.project.AzCar.Dto.Users.LoginApiDto;
import com.project.AzCar.Dto.Users.ResetPasswordApiDto;
import com.project.AzCar.Dto.Users.SignUpApiDto;
import com.project.AzCar.Dto.Users.TokenApiDto;
import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;
import com.project.AzCar.Utilities.OcrService;

import jakarta.servlet.http.HttpServletRequest;
import net.sourceforge.tess4j.TesseractException;

@RestController
@RequestMapping("/api/auth")
public class ApiUsersController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OcrService ocrService;
	@Autowired
	private FilesStorageServices fileStorageServices;
	@Autowired
	private CarImageServices carImageServices;
	@Autowired
	private ExtraFeeServices extraFeeServices;
	@Autowired
	private PlusServiceServices plusServiceServices;
	@Autowired
	private BrandServices brandServices;
	@Autowired
	private CarServices carServices;

	@GetMapping("/getUsers")
	public List<UserDto> getList() {
		List<UserDto> list = new ArrayList<>();
		List<Users> listUser = userServices.findAllUsers();
		for (var item : listUser) {
			var itemdDto = userServices.mapToDto((int) item.getId());
			list.add(itemdDto);
		}

		return list;
	}

	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginApiDto loginDto, HttpServletRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
		request.getSession().setAttribute("emailLogin", loginDto.getUsernameOrEmail());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpApiDto signUpDto) {
		String email = signUpDto.getEmail();
		if (userServices.existsByEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
		}

		try {

			sendEmail(email);
		} catch (Exception e) {

			return new ResponseEntity<>("Error sending email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		UserDto user = new UserDto();
		user.setFullName(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(signUpDto.getPassword());
		userServices.saveUser(user);

		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
	}

	@GetMapping("/getUsersByEmail")
	public UserDto getUsersByEmail(@RequestParam("email") String email) {
		Users user = userServices.findUserByEmail(email);
		if (user != null) {
			var userDto = userServices.mapToDto((int) user.getId());
			return userDto;

		} else {
			return null;
		}
	}

	@PostMapping("/editUser")
	public ResponseEntity<?> editUser(@RequestBody EditApiDto editDto) {
		Users user = userServices.findById(editDto.getId());
		UserDto userDto = userServices.mapToDto((int) user.getId());
		userDto.setFirstName(editDto.getFirstName());
		userDto.setLastName(editDto.getLastName());
		userDto.setGender(editDto.getGender());
		userDto.setPhone(editDto.getPhone());
		userDto.setDob(editDto.getDob());

		userServices.editProfile(user.getEmail(), userDto);
		return new ResponseEntity<>("User updated successfully", HttpStatus.OK);

	}

	@PostMapping("/forgot_password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordApiDto userForgot,
			HttpServletRequest request) {
		String email = userForgot.getEmail();
		if (email == null || email.isEmpty()) {
			return ResponseEntity.badRequest().body("Email is required.");
		}

		Users user = userServices.findUserByEmail(email);
		if (user != null) {
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 5; i++) {
				sb.append(random.nextInt(10));
			}
			String token = sb.toString();
			System.out.println("Email: " + email);
			System.out.println("Token: " + token);
			try {
				userServices.updateResetPasswordToken(token, email);
				sendEmail(email, token);
				return ResponseEntity.ok("We have sent a reset password code to your email.");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failed to send reset password link. Please try again later.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Email not found. Please check your email address.");
		}
	}

	private Set<String> usedTokens = new HashSet<>();

	@PostMapping("/tokenProcess")
	public ResponseEntity<String> tokenProcess(@RequestBody TokenApiDto tokenProcess, HttpServletRequest request) {
		String token = tokenProcess.getToken();
		if (usedTokens.contains(token)) {
			usedTokens.add(token); // Đánh dấu token đã được sử dụng
			return ResponseEntity.badRequest().body("Token has already been used.");
		}
		if (token == null || token.isEmpty()) {
			return ResponseEntity.badRequest().body("Email is required.");
		}
		Users user = userServices.findUserByToken(token);
		if (user != null) {
			return ResponseEntity.ok("User found and actions performed.");
		} else {

			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordApiDto resetDto, HttpServletRequest request) {
		Users user = userServices.findUserByToken(resetDto.getToken());

		user.setPassword(passwordEncoder.encode(resetDto.getPassword()));
		userServices.saveUserReset(user);
		return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);

	}

	// send mail Register
	@PostMapping("/upload")
	public ResponseEntity<DriverLicenseFront> upload(@RequestParam("file") MultipartFile file)
			throws IOException, TesseractException {

		DriverLicenseFront driverLicenseFront = new DriverLicenseFront();
		var ocrResult = ocrService.ocr(file).getResult();
		// Regular expressions
		// Regular expressions
		Pattern licenseNumberPattern = Pattern.compile("No:\\s*(\\d+)");
		Pattern fullNamePattern = Pattern.compile("Full name:\\s*([^\\n]+)");
		Pattern dateOfBirthPattern = Pattern.compile("Date of Birth:\\s*(\\d{2}/\\d{2}/\\d{4})");
		Pattern licenseClassPattern = Pattern.compile("Class:\\s*([\\w\\d]+)");
		Pattern expiresPattern = Pattern.compile("Expires:\\s*(\\d{2}/\\d{2}/\\d{4})");
		Pattern isdriverLicense = Pattern.compile("DRIVER'S LICENSE");

		Matcher matcher;

		matcher = licenseNumberPattern.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setLicenseNumber(matcher.group(1).trim());
		}

		matcher = fullNamePattern.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setFullName(matcher.group(1).trim());
		}

		matcher = dateOfBirthPattern.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setDateOfBirth(matcher.group(1).trim());
		}

		matcher = licenseClassPattern.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setLicenseClass(matcher.group(1).trim());
		}

		matcher = expiresPattern.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setExpires(matcher.group(1).trim());
		}

		matcher = isdriverLicense.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseFront.setDriverLicense(true);
		}
		System.out.println(driverLicenseFront);
		return ResponseEntity.ok(driverLicenseFront);
	}

	@PostMapping("/upload2")
	public ResponseEntity<DriverLicenseBack> upload2(@RequestParam("file") MultipartFile file)
			throws IOException, TesseractException {

		DriverLicenseBack driverLicenseBack = new DriverLicenseBack();
		var ocrResult = ocrService.ocr(file).getResult();
		Pattern isdriverLicense = Pattern.compile("CLASSIFICATION OF MOTOR VEHICLES");
		Matcher matcher;
		matcher = isdriverLicense.matcher(ocrResult);
		if (matcher.find()) {
			driverLicenseBack.setDriverLicense(true);
		}
		return ResponseEntity.ok(driverLicenseBack);
	}

	@PostMapping("/upload3")
	public ResponseEntity<String> upload3(@RequestParam("frontImg") MultipartFile frontImg,
			@RequestParam("behindImg") MultipartFile behindImg, @RequestParam("leftImg") MultipartFile leftImg,
			@RequestParam("rightImg") MultipartFile rightImg, @RequestParam("insideImg") MultipartFile insideImg,
			@RequestParam("data") String jsonData) throws IOException, TesseractException {

		System.out.println(frontImg.getOriginalFilename());
		System.out.println(behindImg.getOriginalFilename());
		System.out.println(leftImg.getOriginalFilename());
		System.out.println(rightImg.getOriginalFilename());
		System.out.println(insideImg.getOriginalFilename());
		System.out.println(jsonData);
		String message = "Great, your car is register successfully!";
		CarInforFlutter modelData = new CarInforFlutter();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			modelData = objectMapper.readValue(jsonData, CarInforFlutter.class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String modelId = brandServices.getModelId(modelData.getBrand(), modelData.getCategory(), modelData.getModel(),
				modelData.getYear());
		System.out.println(modelId);
		System.out.println(modelData);
		CarInfor newRegisterCar = new CarInfor();
		int min = 0; // Minimum value
		int max = 999999999; // Maximum value

		Random rand = new Random();
		int number = rand.nextInt(max - min + 1) + min;
		newRegisterCar.setId(number);
		newRegisterCar.setModelId(modelId);
		newRegisterCar.setServices(modelData.getServices().replace("[", "").replace("]", ""));
		newRegisterCar.setAddress(modelData.getAddress());
		newRegisterCar.setSeatQty(modelData.getSeatQty());
		newRegisterCar.setStatus(Constants.carStatus.VERIFY);
		newRegisterCar.setLicensePlates(modelData.getLicensePlate());
		newRegisterCar.setDescription(modelData.getDescription());
		newRegisterCar.setRules(modelData.getRules());
		newRegisterCar.setPrice(modelData.getDefaultPrice());
		newRegisterCar.setDiscount((int) modelData.getDiscount());
		newRegisterCar.setFuelType(modelData.getFuelType());
		if (modelData.getFuelType() == "Electric") {
			newRegisterCar.setEngineInformationTranmission(true);
		} else {
			newRegisterCar.setEngineInformationTranmission(false);
		}

		newRegisterCar.setCarOwnerId(Integer.parseInt(modelData.getUserId()));
		CarImages frontImgModel = new CarImages();
		CarImages behindImgModel = new CarImages();
		CarImages leftImgModel = new CarImages();
		CarImages rightImgModel = new CarImages();
		CarImages insideImgModel = new CarImages();

		String dir = "./UploadFiles/carImages" + "/" + modelId + "-" + newRegisterCar.getId();
		Path path = Paths.get(dir);

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

		try {

			fileStorageServices.save(frontImg, dir);
			frontImgModel.setName("frontImg");
			frontImgModel.setUrlImage(frontImg.getOriginalFilename());
			frontImgModel.setCarId(number);
			carImageServices.saveImg(frontImgModel);

			fileStorageServices.save(behindImg, dir);
			behindImgModel.setName("behindImg");
			behindImgModel.setUrlImage(behindImg.getOriginalFilename());
			behindImgModel.setCarId(number);
			carImageServices.saveImg(behindImgModel);

			fileStorageServices.save(leftImg, dir);
			leftImgModel.setName("leftImg");
			leftImgModel.setUrlImage(leftImg.getOriginalFilename());
			leftImgModel.setCarId(number);
			carImageServices.saveImg(leftImgModel);

			fileStorageServices.save(rightImg, dir);
			rightImgModel.setName("rightImg");
			rightImgModel.setUrlImage(rightImg.getOriginalFilename());
			rightImgModel.setCarId(number);
			carImageServices.saveImg(rightImgModel);

			fileStorageServices.save(insideImg, dir);
			insideImgModel.setName("insideImg");
			insideImgModel.setUrlImage(insideImg.getOriginalFilename());
			insideImgModel.setCarId(number);
			carImageServices.saveImg(insideImgModel);

		} catch (Exception e) {
			System.out.println(e);
		}

		if (modelData.getDecorationFee().compareTo(BigDecimal.ZERO) > 0
				|| modelData.getCleaningFee().compareTo(BigDecimal.ZERO) > 0) {
			newRegisterCar.setExtraFee(true);
			ExtraFee extraFee = new ExtraFee();
			extraFee.setCarRegisterId(number);
			extraFee.setCleanningFee(
					(modelData.getDecorationFee().longValue()) * (newRegisterCar.getPrice().longValue()) / 100);
			extraFee.setDecorationFee(
					(modelData.getCleaningFee().longValue()) * (newRegisterCar.getPrice().longValue()) / 100);
			extraFeeServices.save(extraFee);

		} else {
			newRegisterCar.setExtraFee(false);
		}
		if (modelData.getDeliveryFee().compareTo(BigDecimal.ZERO) > 0) {
			newRegisterCar.setCarPlus(true);
			PlusServices plusServices = new PlusServices();
			plusServices.setCarRegisterId(number);
			plusServices
					.setFee((modelData.getDeliveryFee().longValue()) * (newRegisterCar.getPrice().longValue()) / 100);
			plusServiceServices.save(plusServices);
		} else {
			newRegisterCar.setCarPlus(false);
		}

		try {
			System.out.println(newRegisterCar);
			carServices.saveCarRegister(newRegisterCar);
			var carDto = carServices.mapToDto(newRegisterCar.getId());
			carDto.setCarmodel(brandServices.getModel(newRegisterCar.getModelId()));
			sendEmail(modelData.getUserEmail(), carDto);

		} catch (Exception e) {
			System.out.println(e);
		}
		return ResponseEntity.ok(message);
	}

	private void sendEmail(String email, CarInforDto carDetails)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Successfull register your car";
		String content = "<p>Hello," + email + "</p>" + "<p>Thank you for registering your car rental with AzCar.</p>"
				+ "<p>Below are some main details of your car:</p>" + "<p><b>Car Details:</b></p>" + "<p>" + "Brand: "
				+ carDetails.getCarmodel().getBrand() + "</p>" + "<p>" + "Model: " + carDetails.getCarmodel().getModel()
				+ "</p>" + "<p>" + "Price: " + carDetails.getPrice() + " $/day" + "</p>" + "<p>" + "License Plates: "
				+ carDetails.getLicensePlates() + "</p>" + "<p>" + "Pick-up Location: " + carDetails.getAddress()
				+ "</p>" +

				"<p>This is to confirm that we already got info of your car, We will send you an email after verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);

	}

	private void sendEmail(String email) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Welcom to AzCar";
		String content = "CaÌ‰m Æ¡n baÌ£n Ä‘aÌƒ sÆ°Ì‰ duÌ£ng diÌ£ch vuÌ£ cuÌ‰a chuÌ�ng tÃ´i";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	private void sendEmail(String email, String token)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Here's the link to reset your password";
		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Here is your password reset token:</p>" + "<p><b>" + token + "</b></p>"
				+ "<p>Use this token to reset your password.</p>"
				+ "<p>Ignore this email if you do remember your password, or you have not made the request</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
}
