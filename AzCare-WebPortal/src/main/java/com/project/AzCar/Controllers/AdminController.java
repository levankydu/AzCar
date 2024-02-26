package com.project.AzCar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@GetMapping("/dashboard/")
	public String getDashboard() {
		return "authentications/dashboard";
	}

}
