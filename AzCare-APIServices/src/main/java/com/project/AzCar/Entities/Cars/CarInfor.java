package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbbasiccarInfor")
public class CarInfor implements Serializable {

	private static final long serialVersionUID = 7180110793287986682L;

	@Id
	private int Id;

	private int seatQty;

	private String fuelType;

	private boolean engineInformationTranmission;
	// false: manual
	// true :auto

	private String services;


	private String licensePlates;
	private int carOwnerId;
	private String modelId;
	private BigDecimal price;
	private String description;
	private boolean isCarPlus;
	private boolean isExtraFee;

	private int discount;
	private String rules;
	private String address;
	private String status;
	

}
