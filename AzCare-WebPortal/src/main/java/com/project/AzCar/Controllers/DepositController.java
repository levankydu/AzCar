package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.AzCar.Dto.PaymentDetails.PaymentDetailsDTO;
import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Service.Deposit.IDepositService;

@Controller
public class DepositController {
	@Autowired 
	IDepositService depositService;
	
	@GetMapping("/dashboard/depositmanager")
	public String getdasshboardDeposit(Model model)
	{
	List<Deposit> depo = depositService.findListDeposit();
	List<PaymentDetailsDTO> ldto = new ArrayList<>();
	if(!depo.isEmpty())
	{
		for(Deposit d : depo)
		{
			PaymentDetailsDTO dto  = new PaymentDetailsDTO();
			dto.setId(d.getId());
			dto.setAmount(d.getAmount());
			dto.setEmail(d.getUser().getEmail());
			dto.setReferenceNumber(d.getReferenceNumber());
			dto.setStatus(d.getStatus().toString());
			ldto.add(dto);
		}
		
	}
	
	model.addAttribute("listDeposit", ldto);
		return "admin/DepositManager";
	}
	
}
