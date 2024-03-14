package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Users.UserRepository;

@Controller
public class AdminController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/dashboard/")
	public String getDashboard(Model model, Authentication authentication) {

		Users loginedUser = new Users();

		loginedUser.setFirstName(authentication.getName());
		model.addAttribute("user", loginedUser);

		return "admin/dashboard";
	}

	@GetMapping("/dashboard/ListAccount")
	public String getfindAll(Model model) {

		List<Users> userlists = userRepo.findAll();
		model.addAttribute("userlists", userlists);
		return "admin/ListAccount";
	}
	
	@GetMapping("/dashboard/ListAccount/profile/{email}")
	public String profile(@PathVariable("email") String email, Model model) {
		Users user = userRepo.findByEmail(email);

		user.getEmail();
		model.addAttribute("user", user);
		return "admin/profile/{email}";
	}

}
