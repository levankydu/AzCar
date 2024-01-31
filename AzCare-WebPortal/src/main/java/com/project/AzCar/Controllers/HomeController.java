package com.project.AzCar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String getHome() {
		return "index";
	}
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		
//		model.addAttribute("user", new User());
		return "/authentications/login";
	}
	@GetMapping("/register")
	public String getRegister() {
		return "/authentications/register";
	}
}
