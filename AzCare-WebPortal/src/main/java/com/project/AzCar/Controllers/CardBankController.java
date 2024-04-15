package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.ICarbankService;
import com.project.AzCar.Services.Users.UserServices;
@Controller
public class CardBankController {
	@Autowired
	private ICarbankService cardService;
	@Autowired
	private UserServices uService;
	
	@GetMapping("/dashboard/depositmanager/cardbank")
	public String getdasshboardDepositCard(Model model)
	{
	List<Cardbank> depo = cardService.getListCardBank();
	model.addAttribute("listCardbank", depo);
		return "admin/cardbank";
	}
	
	@GetMapping("/user/profile/edit/cardbank/{email}")
	public String editProfile(@PathVariable("email") String email, Model model) {
		Users user = uService.findUserByEmail(email);
		Cardbank lcard = user.getCardBank();
		if (user != null) {
			
				model.addAttribute("user",user);
				model.addAttribute("cardbank",lcard);
				return "authentications/cardbankedit";
			
		}
		return "redirect:/";
		
			
		} 
	@PostMapping("/user/profile/edit/cardbank/{email}")
	@ResponseBody
	public ResponseEntity<String> editProfile(@PathVariable("email") String email,
			@ModelAttribute("uDto") UserDto uDto) {
		try {
			uService.editProfile(email, uDto);
			return ResponseEntity.ok("Profile updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating profile: " + e.getMessage());
		}
	}
}
