package com.project.AzCar.payments.vnpay;

import lombok.Builder;

public abstract class VnpayDTO {
	@Builder
	public static class VNPayResponse {
		public String code;
		public String message;
		public String paymentUrl;
	}
}
