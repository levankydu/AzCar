package com.project.AzCar.Dto.Coupons;

import lombok.Data;

@Data
public class CouponDTO {
	private int id;
	private String couponCode;
	private String Status;
	private Double discountPercentage;
	private String Content;
	private int quantity;

}
