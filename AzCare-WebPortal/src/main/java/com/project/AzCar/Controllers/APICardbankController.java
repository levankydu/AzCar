package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Dto.PaymentDetails.CardBankDTO;
import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.ICarbankService;
import com.project.AzCar.Services.Users.UserServices;

@Controller
public class APICardbankController {

	@Autowired
	private ICarbankService cardService;
	@Autowired
	private UserServices uService;

	@GetMapping("/cardBank/user/{userId}")
	public ResponseEntity<Cardbank> findCardBankByUserId(@PathVariable("userId") String userId) {
		Long longUserId = Long.parseLong(userId); // Chuyển đổi String sang Long

		Cardbank cardBanks = cardService.findCardbankByUserId(longUserId);
		if (cardBanks != null) {
			return new ResponseEntity<>(cardBanks, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/cardBank/admin")
	public ResponseEntity<List<Cardbank>> getListCardBankAdmin() {
		List<Cardbank> cards = cardService.getListCardBankAdmin();
		return ResponseEntity.ok(cards); // Returns the list of card banks with HTTP status 200
	}

	@PostMapping("/dashboard/createBank")
	public ResponseEntity<?> createKeyword(@RequestBody CardBankDTO dto) {
		Cardbank cb = new Cardbank();
		cb.setBankName(dto.getBankName());
		cb.setAddressbank(dto.getAddressbank());
		cb.setBankNumber(dto.getBankNumber());
		cb.setBeneficiaryName(dto.getBeneficiaryName());
		cb.setActive(EnumCoupon.InActive);
		cardService.saveCardbank(cb);

		return new ResponseEntity<Cardbank>(cb, HttpStatus.OK);
	}

	@PostMapping("/cardBank/create")
	public ResponseEntity<?> createCardbank(@RequestParam("bankName") String bankName,
			@RequestParam("bankNumber") String bankNumber, @RequestParam("beneficiaryName") String beneficiaryName,
			@RequestParam("addressbank") String addressbank, @RequestParam("userId") String userId) {

		Long longUser = Long.parseLong(userId);
		Cardbank cb = new Cardbank();
		Users u = uService.findById(longUser);

		cb.setBankName(bankName);
		cb.setAddressbank(addressbank);
		cb.setBankNumber(bankNumber);
		cb.setBeneficiaryName(beneficiaryName);
		cb.setActive(EnumCoupon.Active);
		cb.setUser(u);
		cardService.saveCardbank(cb);

		return new ResponseEntity<Cardbank>(cb, HttpStatus.OK);
	}

	@RequestMapping(value = "/cardBank/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCardbank(@PathVariable("id") int id, @RequestBody CardBankDTO cardbankDTO) {

		Cardbank cb = cardService.findCardbankbyId(id);
		Users u = uService.findById(cardbankDTO.getUserId());
		cb.setBankName(cardbankDTO.getBankName());
		cb.setAddressbank(cardbankDTO.getAddressbank());
		cb.setBankNumber(cardbankDTO.getBankNumber());
		cb.setBeneficiaryName(cardbankDTO.getBeneficiaryName());
		cb.setActive(EnumCoupon.Active);
		cb.setUser(u);
		cardService.saveCardbank(cb);

		return ResponseEntity.ok().build();
	}

	@PutMapping(value = "/dashboard/acceptmethodPayment/{id}")
	public ResponseEntity<?> acceptPayment(@PathVariable("id") int id) {

		Cardbank cb = cardService.findCardbankbyId(id);
		List<Cardbank> cards = cardService.getListCardBankAdmin();
		for (Cardbank item : cards) {
			if (item.getId() != id && cb.getActive() == EnumCoupon.InActive) {
				item.setActive(EnumCoupon.InActive);
				cardService.saveCardbank(item);
			}
		}
		System.out.println(cb);
		if (cb != null) {
			if (cb.getActive() == EnumCoupon.Active) {
				cb.setActive(EnumCoupon.InActive);
			} else if (cb.getActive() == EnumCoupon.InActive) {
				cb.setActive(EnumCoupon.Active);
			}

			cardService.saveCardbank(cb);
			return new ResponseEntity<Cardbank>(cb, HttpStatus.OK);
		}

		return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
	}

}
