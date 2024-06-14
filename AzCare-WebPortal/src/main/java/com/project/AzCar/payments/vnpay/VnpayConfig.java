package com.project.AzCar.payments.vnpay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;
import com.project.AzCar.payments.vnpay.util.VNPayUtil;

import lombok.Getter;

@Configuration
public class VnpayConfig {
	@Getter
	@Value("${payment.vnPay.url}")
	private String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
	@Value("${payment.vnPay.returnUrl}")
	private String vnp_ReturnUrl = "http://localhost:8081/api/v1/payment/vn-pay-callback\r\n" + "";
	@Value("${payment.vnPay.tmnCode}")
	private String vnp_TmnCode = "J8HZEM40";
	@Getter
	@Value("${payment.vnPay.secretKey}")
	private String secretKey = "RJK1WYUHFQHYISCKJAJF1UQI6DNR530Z";
	@Value("${payment.vnPay.version}")
	private String vnp_Version = "2.1.0";
	@Value("${payment.vnPay.command}")
	private String vnp_Command = "pay";
	@Value("${payment.vnPay.orderType}")
	private String orderType = "other";

	public Map<String, String> getVNPayConfig() {
		Map<String, String> vnpParamsMap = new HashMap<>();
		vnpParamsMap.put("vnp_Version", this.vnp_Version);
		vnpParamsMap.put("vnp_Command", this.vnp_Command);
		vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
		vnpParamsMap.put("vnp_CurrCode", "VND");
		vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
		vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
		vnpParamsMap.put("vnp_OrderType", this.orderType);
		vnpParamsMap.put("vnp_Locale", "vn");
		vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnpCreateDate = formatter.format(calendar.getTime());
		vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
		calendar.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(calendar.getTime());
		vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
		return vnpParamsMap;
	}
}
