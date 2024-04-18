package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.ICarbankService;
import com.project.AzCar.Services.Users.UserServices;

@Controller
public class CardBankController {
	@Autowired
	private ICarbankService cardService;
	@Autowired
	private UserServices uServices;

	@GetMapping("/dashboard/cardbank")
	public String getdasshboardDepositCard(Model model) {
<<<<<<< HEAD
		List<Cardbank> depo = cardService.getListCardBankAdmin();
=======
		List<Cardbank> depo = cardService.getListCardBank();
>>>>>>> 46f90fc3fb8dc50ff5e40fa667557365d0ae2229
		model.addAttribute("listCardbank", depo);
		return "admin/cardbank";
	}

	@GetMapping("/user/profile/edit/cardbank/{email}")
	public String editProfile(@PathVariable("email") String email, Model model) {
		Users user = uServices.findUserByEmail(email);

		if (user != null) {
			model.addAttribute("user", user);
			Cardbank c = cardService.findCardbankByUserId((int) user.getId());
			model.addAttribute("cardbank", c);
			return "authentications/cardbankedit";
		} else {

			return "redirect:/user/profile/" + email;
		}
	}
}
