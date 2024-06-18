package com.project.AzCar.Controllers;

import java.time.format.DateTimeFormatter;
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
	

	@GetMapping("/dashboard/payment/listwithdraw")
	public String getdasshboardDeposit(Model model) {
		List<Deposit> depo = depositService.findListDepositWithDraw();
		List<PaymentDetailsDTO> ldto = new ArrayList<>();
		if (!depo.isEmpty()) {
			for (Deposit d : depo) {
				PaymentDetailsDTO dto = new PaymentDetailsDTO();
				dto.setId(d.getId());
				dto.setEmail(d.getUser().getEmail());
				dto.setReferenceNumber(d.getReferenceNumber());
				dto.setStatus(d.getStatus().toString());
				dto.setWithdraw(d.getWithdraw());
				  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m ddMMyy");
				String formatDate = d.getPaymentDateAt().format(formatter);
				dto.setTimeCreated(formatDate);
				ldto.add(dto);
			}

		}

		model.addAttribute("listDeposit", ldto);
		return "admin/DepositManager";
	}
	@GetMapping("/dashboard/payment/transaction")
	public String getdasshboardTransaction(Model model) {
		List<Deposit> depo = depositService.findListDepositTransaction();
		List<PaymentDetailsDTO> ldto = new ArrayList<>();
		if (!depo.isEmpty()) {
			for (Deposit d : depo) {
				PaymentDetailsDTO dto = new PaymentDetailsDTO();
				dto.setId(d.getId());
				dto.setAmount(d.getAmount());
				dto.setEmail(d.getUser().getEmail());
				dto.setReferenceNumber(d.getReferenceNumber());
				dto.setStatus(d.getStatus().toString());
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m ddMMyy");
					String formatDate = d.getPaymentDateAt().format(formatter);
					dto.setTimeCreated(formatDate);
				ldto.add(dto);
			}

		}

		model.addAttribute("listtransaction", ldto);
		return "admin/listTransaction";
	}
	@GetMapping("/dashboard/payment/deposit")
	public String getDashboardDeposit(Model model) {
		List<Deposit> depo = depositService.findListDepositTransaction();
		List<PaymentDetailsDTO> ldto = new ArrayList<>();
		if (!depo.isEmpty()) {
			for (Deposit d : depo) {
				PaymentDetailsDTO dto = new PaymentDetailsDTO();
				dto.setId(d.getId());
				dto.setAmount(d.getAmount());
				dto.setEmail(d.getUser().getEmail());
				dto.setReferenceNumber(d.getReferenceNumber());
				dto.setStatus(d.getStatus().toString());
				dto.setWithdraw(d.getWithdraw());
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m ddMMyy");
					String formatDate = d.getPaymentDateAt().format(formatter);
					dto.setTimeCreated(formatDate);
				ldto.add(dto);
			}

		}

		model.addAttribute("listDepositAll", ldto);
		return "admin/DepositAllManager";
	}

}
