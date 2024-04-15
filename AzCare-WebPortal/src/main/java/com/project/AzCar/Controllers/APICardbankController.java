package com.project.AzCar.Controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.KeywordIgnore.KeyWordIgnoreDTO;
import com.project.AzCar.Dto.PaymentDetails.CardBankDTO;
import com.project.AzCar.Dto.PaymentDetails.PaymentDetailsDTO;
import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Service.Deposit.ICarbankService;

import jakarta.mail.MessagingException;

@RestController
public class APICardbankController {

	@Autowired
	private ICarbankService cardService;
	
	@PostMapping(value="/dashboard/createBank")
	public ResponseEntity<?> createKeyword(@RequestBody CardBankDTO dto)
	{
		Cardbank cb = new Cardbank();
		cb.setBankName(dto.getBankName());
		cb.setAddressbank(dto.getAddressbank());
		cb.setBankNumber(dto.getBankNumber());
		cb.setBeneficiaryName(dto.getBeneficiaryName());
		cb.setActive(EnumCoupon.InActive);
		cardService.saveCardbank(cb);
		
		return new ResponseEntity<Cardbank> (cb,HttpStatus.OK);
	}
	
	@PutMapping(value="/dashboard/acceptmethodPayment/{id}")
	public ResponseEntity<?> acceptPayment(@PathVariable("id") int  id) 
	{
		
		Cardbank cb = cardService.findCardbankbyId(id);
		System.out.println(cb);
		if (cb != null) {
		    if (cb.getActive() == EnumCoupon.Active) {
		        cb.setActive(EnumCoupon.InActive);
		    } else if (cb.getActive() == EnumCoupon.InActive) {
		        cb.setActive(EnumCoupon.Active);
		    }
		    
		    cardService.saveCardbank(cb);
		    System.out.println(cb);
		    return new ResponseEntity<Cardbank>(cb, HttpStatus.OK);
		}

		
		return new ResponseEntity<String> ("Bad Request",HttpStatus.BAD_REQUEST);
	}
	
}
