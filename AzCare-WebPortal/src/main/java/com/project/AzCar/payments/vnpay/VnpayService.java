package com.project.AzCar.payments.vnpay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Entities.Deposit.EnumDeposit;
import com.project.AzCar.Service.Deposit.IDepositService;
import com.project.AzCar.payments.vnpay.VnpayDTO.VNPayResponse;
import com.project.AzCar.payments.vnpay.util.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VnpayService {
	@Autowired
	private VnpayConfig vnPayConfig;
	@Autowired
	private IDepositService depositService;

	public VnpayDTO.VNPayResponse createVnPayPayment(HttpServletRequest request, Deposit deposit) {
		long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
		String bankCode = request.getParameter("bankCode");
		Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
		vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
		if (bankCode != null && !bankCode.isEmpty()) {
			vnpParamsMap.put("vnp_BankCode", bankCode);
		}
		vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
		System.out.println("IP address: " + VNPayUtil.getIpAddress(request));
		// build query url
		String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
		System.out.println("query URL : " + vnPayConfig.getVnp_PayUrl());
		String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
		queryUrl += "&vnp_SecureHash=" + VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
		String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
		System.out.println("url : " + paymentUrl);
		Deposit temp = depositService.findByRefenceId(vnpParamsMap.get("vnp_TxnRef"));
		System.out.println(vnpParamsMap.get("vnp_TxnRef"));
		System.out.println(vnpParamsMap.get("vnp_Amount"));
		
		if (temp == null) {
			deposit.setStatus(EnumDeposit.Pending);
			  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			deposit.setPaymentDateAt(LocalDateTime.parse(vnpParamsMap.get("vnp_ExpireDate"),formatter));
			deposit.setAmount(BigDecimal.valueOf(Integer.parseInt(request.getParameter("amount"))));
			deposit.setReferenceNumber(vnpParamsMap.get("vnp_TxnRef"));
			deposit.setDecription(vnpParamsMap.get("vnp_OrderInfo"));
			depositService.savePaymentDetails(deposit);

		}
		
		
		
		

		return VnpayDTO.VNPayResponse.builder().code("ok").message("success").paymentUrl(paymentUrl).build();
	}
	public VNPayResponse createVnPayRefund(HttpServletRequest request,Deposit d)
	{	
	 Deposit d1 = 	depositService.findByRefenceId(d.getReferenceNumber());
	 	if(d1 !=null)
	 	{
	 		Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
			vnpParamsMap.put("vnp_Amount", String.valueOf(d1.getAmount()));
			vnpParamsMap.put("vnp_TxnRef", d1.getReferenceNumber());
			vnpParamsMap.put("vnp_OrderInfo", "Hoan tien giao dich:  " +d1.getReferenceNumber() );
			   String dateString = d1.getPaymentDateAt().toString();
		        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		        try {
		            Date date = inputFormat.parse(dateString);
		            calendar.setTime(date);
		            
		            // Định dạng lại thời gian theo "yyyyMMddHHmmss"
		            String formattedDate = outputFormat.format(calendar.getTime());
		            vnpParamsMap.put("vnp_TransactionDate",formattedDate);
		            System.out.println("Formatted Date: " + formattedDate);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    	vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
			
			
			
	 		
	 	}
		return null;
	}
	
	
}
