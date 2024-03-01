package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;

	private int seatQty;

	private String fuelType;

	private boolean engineInformationTranmission;

	private String services;

	private boolean isAvailabled;

	private String licensePlates;

	@Transient
	@OneToMany(mappedBy = "cars", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private int carOwnerId;

	@Transient
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,targetEntity = CarImages.class)
	private List<CarImages> carImages = new ArrayList<>();
	
	@Transient
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST,mappedBy = "carModelList")
	private int modelId;

	private BigDecimal price;
	private String description;
	private int maxDistance;

}
