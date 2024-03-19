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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

		if(uServices.saveAdmin(new UserDto())) {
			JSONObject entity = new JSONObject();
			entity.put("Code 1", "Created Admin Account");

			return ResponseEntity.ok(entity);
		}
		else {
			JSONObject entity = new JSONObject();
			entity.put("Code 0", "Created Admin Account Fail, Addmin Account already created");

			return ResponseEntity.ok(entity);
		}
		
		

		
	}

	@GetMapping("/user/profile/{email}")
	public String profile(@PathVariable("email") String email, Model model) {

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
		return "/authentications/changePassword";
	}

	@PostMapping("/user/profile/changePassword/{email}")
	public String changePassword(@PathVariable("email") String email,@RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {

		 if (!newPassword.equals(confirmPassword)) {
		        redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
		        return "redirect:/user/profile/changePassword/{email}";
		    }
		 if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
			 redirectAttributes.addFlashAttribute("error", "newPasswords or ConfirmPassword not blank.");
		        return "redirect:/user/profile/changePassword/{email}";
		}
		    // Encode the new password before saving
		    String encodedPassword = passwordEncoder.encode(newPassword);

		    // Check if user exists before changing password
		    if (uServices.findUserByEmail(email) != null) {
		        uServices.changePassword(email, encodedPassword);
		        redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
		    } else {
		        redirectAttributes.addFlashAttribute("error", "User with email " + email + " not found.");
		    }
		    return "redirect:/user/profile/{email}";
	}
}
