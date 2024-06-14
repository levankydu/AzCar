package com.project.AzCar.payments.vnpay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Entities.Deposit.EnumDeposit;
import com.project.AzCar.Service.Deposit.IDepositService;
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
	
}
