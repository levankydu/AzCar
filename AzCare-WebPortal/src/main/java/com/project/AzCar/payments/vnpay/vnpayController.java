package com.project.AzCar.payments.vnpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URISyntaxException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import java.util.HashMap;

import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.AzCar.Dto.PaymentDetails.PaymentDetailsDTO;
import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Entities.Deposit.EnumDeposit;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Service.Deposit.IDepositService;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Payments.ProfitCallBack;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.payments.vnpay.util.VNPayUtil;

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
	
	
	@PostMapping("/pay-refund")
	public ResponseEntity<?> getPay_return(HttpServletRequest request,@RequestBody PaymentDetailsDTO d) throws IOException, InterruptedException, URISyntaxException
	{   
		System.out.println("ID: "+ d.getReferenceNumber());
		Deposit d1 = depositService.findByRefenceId(d.getReferenceNumber());
		if(d1==null)
		{
			return new ResponseEntity<String>("Deposit not found",HttpStatus.NOT_FOUND);
		}
		   // Command: refund
        String vnp_RequestId = VNPayUtil.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = VNPayContains.vnp_TmnCode;
        String vnp_TransactionType = "03";
        String vnp_TxnRef = d1.getReferenceNumber();
        String vnp_TransactionNo = "228811";
        long amount = d1.getAmount().multiply(new BigDecimal(100)).longValue();
//        String vnp_Amount = String.valueOf(amount);
        String vnp_Amount = "5000000";
        String vnp_OrderInfo = "Hoan tien GD :" + vnp_TxnRef;
        LocalDateTime dateString = d1.getPaymentDateAt();
        System.out.println("Date : "  + dateString);
        System.out.println("Amount : "  + amount);
        
        String vnp_TransactionDate = "";
        try {
        	 // Chuyển đổi LocalDateTime sang ZonedDateTime với múi giờ "Etc/GMT+7"
            ZoneId zoneId = ZoneId.of("Etc/GMT+7");
            ZonedDateTime zonedDateTime = dateString.atZone(zoneId);
                 
            // Định dạng lại thời gian theo "yyyyMMddHHmmss"
         // Định dạng lại thời gian theo "yyyyMMddHHmmss"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDate = zonedDateTime.format(formatter);
             vnp_TransactionDate = formattedDate;
            System.out.println("Formatted Date: " + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String vnp_CreateBy = "Admin";
        String vnp_IpAddr = VNPayUtil.getIpAddress(request);
        JsonObject  vnp_Params = new JsonObject ();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", 5000000);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        if (vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty()) {
            vnp_Params.addProperty("vnp_TransactionNo", vnp_TransactionNo);
//        	vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        }
//    	vnp_Params.put("vnp_TransactionDate", vnp_TransactionDate);
//    	vnp_Params.put("vnp_CreateBy", "admin");
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", "Admin");
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
//    	vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//    	vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
    	
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr); 
    	  String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, 
                  vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate, 
                  vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
          
        String vnp_SecureHash = VNPayUtil.hmacSHA512(VNPayContains.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);
        System.out.println(vnp_Params.getAsJsonObject());
//        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        
        System.out.println("hashData : " + hash_Data);
        URL url = new URL(VNPayContains.URL_REFUND);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
//        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
//     
        

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(),"utf-8"));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output.trim());
        }
        in.close();
        System.out.println(response.toString());
       
        return new ResponseEntity<String>(response.toString(),HttpStatus.OK);
		
	}
	@GetMapping("/vn-pay-callback")
	public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
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
		    // Gửi thông điệp "COMPLETED" về tab gốc
	        String script = "<script>window.opener.postMessage({status: 'COMPLETED'}, 'http://localhost:8081'); window.close();</script>";
	        return ResponseEntity.ok().body(script);
			

		} else {
			d.setStatus(EnumDeposit.Decline);
			depositService.updateDeposit(d);
			// Redirect URL for failure
			  // Gửi thông điệp "FAILED" về tab gốc
	        String script = "<script>window.opener.postMessage({status: 'FAILED'}, 'http://localhost:8081'); window.close();</script>";
	        return ResponseEntity.ok().body(script);
		}
		

	}
}
