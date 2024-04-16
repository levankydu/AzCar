package com.project.AzCar.Controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.PaymentDetails.PaymentDetailsDTO;
import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Entities.Deposit.EnumDeposit;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.IDepositService;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Payments.ProfitCallBack;
import com.project.AzCar.Services.Users.UserServices;

import jakarta.mail.MessagingException;

@RestController
public class APIDepositController {

	@Autowired
	IDepositService depositService;
	@Autowired
	private PaymentService paymentServices;
	@Autowired
	UserServices userService;
	@Autowired
	private OrderDetailsService orderServices;

	@PostMapping(value = "/home/myplan/createpayments/deposit/{id}")
	public ResponseEntity<?> createDeposit(@PathVariable("id") String id, @RequestBody PaymentDetailsDTO dt) {
		Deposit d = depositService.findByRefenceId(id);
		if (d == null) {
			Deposit temp = new Deposit();
			temp.setAmount(dt.getAmount());
			LocalDateTime time = LocalDateTime.now();
			temp.setPaymentDateAt(time);
			temp.setReferenceNumber(id);
			temp.setStatus(EnumDeposit.Pending);
			Users user = userService.findById(dt.getUserId());
			if (user != null) {
				temp.setUser(user);
			}

			depositService.savePaymentDetails(temp);

			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/dashboard/acceptPayment/{id}")
	public ResponseEntity<?> acceptPayment(@PathVariable("id") String id, @RequestBody PaymentDetailsDTO dto)
			throws UnsupportedEncodingException, MessagingException {

		Deposit d = depositService.findByRefenceId(dto.getReferenceNumber());
		if (d != null) {
			if (!d.getStatus().toString().contains("Done")) {
				d.setStatus(EnumDeposit.Done);
				depositService.updateDeposit(d);
				Users user = userService.findById(d.getUser().getId());
				BigDecimal balance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
				BigDecimal amount = d.getAmount();
				System.out.println("sá»‘ dÆ° hiá»‡n táº¡i" + balance + ", Sá»‘ tiá»�n náº¡p vÃ o" + amount);
				BigDecimal total = balance.add(amount);
				System.out.print(total);
				user.setBalance(total);
				userService.saveUserReset(user);
				String email = user.getEmail();
				String mailContent = "<p>Hello " + email + ",</p>"
						+ "<p>We are pleased to inform you that your deposit has been successfully processed.</p>"
						+ "<p>Below are the details of your deposit:</p>" + "<table>" + "<tr>" + "<th>Bill Name: </th>"
						+ "<td>" + "" + "</td>" + "</tr>" + "<tr>" + "<th>Deposit Amount: </th>" + "<td>$" + amount
						+ "</td>" + "</tr>" + "<tr>" + "<th>Current Balance: </th>" + "<td>$" + user.getBalance()
						+ "</td>" + "</tr>" + "<tr>" + "<th>Total Amount: </th>" + "<td>$" + total + "</td>" + "</tr>"
						+ "<tr>" + "<th>Transaction ID: </th>" + "<td>" + d.getReferenceNumber() + "</td>" + "</tr>"
						+ "</table>"
						+ "<p>This email confirms that your deposit has been successfully processed. Your current balance is updated accordingly.</p>"
						+ "<p>If you have any questions or concerns, please feel free to contact us.</p>"
						+ "<p>Best regards,<br>AzCar Team</p>";
				orderServices.sendOrderEmail(email, "Place Order Successfully", mailContent);
				paymentServices.createNewProfit(user.getId(), amount, new ProfitCallBack() {
					@Override
					public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
					}
				}, true);
				return new ResponseEntity<Deposit>(d, HttpStatus.OK);
			}

		}
		return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
	}

}
