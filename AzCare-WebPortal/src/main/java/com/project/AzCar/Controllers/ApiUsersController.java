package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

import com.project.AzCar.Dto.DriverLicense.DriverLicenseBack;
import com.project.AzCar.Dto.DriverLicense.DriverLicenseFront;
import com.project.AzCar.Dto.Users.EditApiDto;
import com.project.AzCar.Dto.Users.ForgotPasswordApiDto;
import com.project.AzCar.Dto.Users.LoginApiDto;
import com.project.AzCar.Dto.Users.ResetPasswordApiDto;
import com.project.AzCar.Dto.Users.SignUpApiDto;
import com.project.AzCar.Dto.Users.TokenApiDto;
import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Users.UserServices;
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

	@PostMapping("/tokenProcess")
	public ResponseEntity<String> tokenProcess(@RequestBody TokenApiDto tokenProcess, HttpServletRequest request) {
		String token = tokenProcess.getToken();
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
	public ResponseEntity<DriverLicenseBack> upload3(@RequestParam("frontImg") MultipartFile frontImg,
			@RequestParam("behindImg") MultipartFile behindImg, @RequestParam("leftImg") MultipartFile leftImg,
			@RequestParam("rightImg") MultipartFile rightImg, @RequestParam("insideImg") MultipartFile insideImg)
			throws IOException, TesseractException {

		System.out.println(frontImg.getOriginalFilename());
		System.out.println(behindImg.getOriginalFilename());
		System.out.println(leftImg.getOriginalFilename());
		System.out.println(rightImg.getOriginalFilename());
		System.out.println(insideImg.getOriginalFilename());

		return null;
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
