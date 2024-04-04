package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbcarmodellist")
public class CarModelList implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 937172885527097156L;

	@Id
	private String objectId;


	private String brand;
	private String category;
	@Transient
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, targetEntity = CarInfor.class)
	private List<CarInfor> cars = new ArrayList<>();

	private String model;

	private Long year;
	
	
}
