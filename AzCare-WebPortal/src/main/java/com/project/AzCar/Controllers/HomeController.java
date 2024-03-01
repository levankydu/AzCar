package com.project.AzCar.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Users.UserServices;


import jakarta.validation.Valid;
import net.minidev.json.JSONObject;

@Controller
public class HomeController {

	@Autowired
	private UserServices uServices;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String getHome() {
		return "index";
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
			result.rejectValue("email", null, "User already registered !!!");

		}

		if (!userDto.isPasswordMatching()) {
			result.rejectValue("confirmPassword", null, "Confirm Password do not match.");
			return "/authentications/register";
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "/authentications/register";
		}

		uServices.saveUser(userDto);
//		return "/authentications/login";
		return "redirect:/register?success";

	}

	@GetMapping(path = "/registeradmin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<JSONObject> registrationAdmin(@ModelAttribute UserDto userDto) {

		uServices.saveAdmin(new UserDto());

		JSONObject entity = new JSONObject();
		entity.put("Code 1", "Created Admin Account");

		return ResponseEntity.ok(entity);
	}
}
