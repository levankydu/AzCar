package com.project.AzCar.Dto.CarInfos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarModelList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarInforDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 98208010374273604L;

	private int Id;

	private int seatQty;

	private String fuelType;

	private boolean engineInformationTranmission;
	// false: manual
	// true :auto

	private String services;

	private boolean isAvailabled;

	private String licensePlates;
	private int carOwnerId;
	private CarModelList carmodel;
	private BigDecimal price;
	private String description;
	private boolean isCarPlus;
	private boolean isExtraFee;
	private boolean isFastBooking;
	private int discount;
	private String rules;
	private String address;
	private List<CarImages> images;
}
