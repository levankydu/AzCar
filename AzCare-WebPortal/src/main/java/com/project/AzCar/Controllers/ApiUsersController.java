package com.project.AzCar.Controllers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Users.EditApiDto;
import com.project.AzCar.Dto.Users.LoginApiDto;

import com.project.AzCar.Dto.Users.SignUpApiDto;
import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class ApiUsersController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
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

	private void sendEmail(String email) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Welcom to AzCar";
		String content = "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
	
}
