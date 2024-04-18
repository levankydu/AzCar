package com.project.AzCar.Dto.Orders;

import lombok.Data;

@Data
public class PhoneOrder {
	private String fromDate;
	private String toDate;
	private String ward;
	private String district;
	private String province;
	private String carId;
	private String userEmail;
	private String userId;
}
