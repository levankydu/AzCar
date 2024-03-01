package com.project.AzCar.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.AzCar.Entities.Users.Users;

@Controller
public class AdminController {

	@GetMapping("/dashboard/")
	public String getDashboard(Model model,Authentication authentication) {
		
		Users loginedUser = new Users();
	
		loginedUser.setFirstName(authentication.getName());
		model.addAttribute("user", loginedUser);
		
		return "admin/dashboard";
	}

}
