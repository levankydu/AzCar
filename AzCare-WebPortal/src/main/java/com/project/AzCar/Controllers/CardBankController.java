package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Service.Deposit.ICarbankService;
@Controller
public class CardBankController {
	@Autowired
	private ICarbankService cardService;
	
	@GetMapping("/dashboard/cardbank")
	public String getdasshboardDepositCard(Model model)
	{
	List<Cardbank> depo = cardService.getListCardBank();
	model.addAttribute("listCardbank", depo);
		return "admin/cardbank";
	}
}
