package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name= "tbbasiccarInfor")
public class CarInfor implements Serializable {
	
	
	private static final long serialVersionUID = 7180110793287986682L;



	@Id
	private int Id;
	
	private int seatQty;
	
	private String fuelType;
	
	private boolean engineInformationTranmission;
	
	private String services;
	
	private boolean isAvailabled;
	
	private String licensePlates;
	
	private int carOwner;
	
	private int modelId;
	private BigDecimal price;
	private String description;
	private int maxDistance;
	
	
}
