package com.project.AzCar.Dto.CarInfos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarInforFlutter {
	private String licensePlate;
	private String brand;
	private String category;
	private String model;
	private int year;
	private int seatQty;
	private String fuelType;
	private String description;
	private BigDecimal defaultPrice;
	private double discount;
	private String address;
	private BigDecimal deliveryFee;
	private BigDecimal cleaningFee;
	private BigDecimal decorationFee;
	private String rules;
	private String userEmail;
	private String userId;
	private String services;

}
