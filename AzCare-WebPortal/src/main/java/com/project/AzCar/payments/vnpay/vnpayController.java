package com.project.AzCar.payments.vnpay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Entities.Deposit.EnumDeposit;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.IDepositService;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Payments.ProfitCallBack;
import com.project.AzCar.Services.Users.UserServices;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class vnpayController {
	private final VnpayService paymentService;

	@Autowired
	private IDepositService depositService;
	@Autowired
	UserServices userService;
	@Autowired
	private OrderDetailsService orderServices;
	@Autowired
	private PaymentService paymentServices;
//	@GetMapping("/vn-pay")
//	public ResponseObject<VnpayDTO.VNPayResponse> pay(HttpServletRequest request) {
//		System.out.print(request);
//		return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
//	}

	@GetMapping("/vn-pay")
	public ResponseEntity<?> pay(HttpServletRequest request) {
		
		String email = request.getSession().getAttribute("emailLogin").toString();
		System.out.println("email:" + email);
		Users ownerId = userService.findUserByEmail(email);
		System.out.println("looix ow dau");
		Deposit d = new Deposit();
		d.setUser(ownerId);
		depositService.savePaymentDetails(d);
		VnpayDTO.VNPayResponse a = paymentService.createVnPayPayment(request, d);

		return new ResponseEntity<VnpayDTO.VNPayResponse>(a, HttpStatus.OK);
	}

	@GetMapping("/vn-pay-callback")
	public ResponseEntity<VnpayDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String redirectUrl;
		String status = request.getParameter("vnp_ResponseCode");
		String vnpNumber= request.getParameter("vnp_TxnRef");
		
		System.out.println("email:" + vnpNumber);
		String email = request.getSession().getAttribute("emailLogin").toString();
		System.out.println("email:" + email);
		Users user = userService.findUserByEmail(email);
		Deposit d = depositService.findByRefenceId(vnpNumber);
		BigDecimal amount = d.getAmount();
		if (status.equals("00")) {
			
			if(d !=null)
			{
				d.setStatus(EnumDeposit.Done);
				BigDecimal balance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
				
				BigDecimal total = balance.add(amount);
				System.out.print(total);
				depositService.updateDeposit(d);
				user.setBalance(total);
				userService.saveUserReset(user);
				String mailContent = "<p>Hello " + email + ",</p>"
						+ "<p>We are pleased to inform you that your deposit has been successfully processed.</p>"
						+ "<p>Below are the details of your deposit:</p>" + "<table>" + "<tr>"
						+ "<th>Bill Name: </th>" + "<td>" + "" + "</td>" + "</tr>" + "<tr>"
						+ "<th>Deposit Amount: </th>" + "<td>$" + amount + "</td>" + "</tr>" + "<tr>"
						+ "<th>Current Balance: </th>" + "<td>$" + user.getBalance() + "</td>" + "</tr>" + "<tr>"
						+ "<th>Total Amount: </th>" + "<td>$" + total + "</td>" + "</tr>" + "<tr>"
						+ "<th>Transaction ID: </th>" + "<td>" + d.getReferenceNumber() + "</td>" + "</tr>"
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
			}
			// Redirect URL for success
			redirectUrl = "http://localhost:8081/home/myplan/";
			// Build the response object if needed
			VnpayDTO.VNPayResponse response = new VnpayDTO.VNPayResponse("00", "Success",
					"https://localhost:8081/home/myplan/");
			

		} else {
			d.setStatus(EnumDeposit.Decline);
			depositService.updateDeposit(d);
			// Redirect URL for failure
			redirectUrl = "https://localhost:8081/home/myplan/";
//			return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
		}
		return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();

	}
}
