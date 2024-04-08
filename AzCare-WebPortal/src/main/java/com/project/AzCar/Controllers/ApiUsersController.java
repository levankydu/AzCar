package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Users.LoginApiDto;
import com.project.AzCar.Dto.Users.SignUpApiDto;
import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Users.UserServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class ApiUsersController {

	@Autowired
	private UserServices userServices;

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
	public ResponseEntity<String> authenticateUser(@RequestBody LoginApiDto loginDto,HttpServletRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
		request.getSession().setAttribute("emailLogin", loginDto.getUsernameOrEmail());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpApiDto signUpDto) {

		if (userServices.existsByEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
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
	

}