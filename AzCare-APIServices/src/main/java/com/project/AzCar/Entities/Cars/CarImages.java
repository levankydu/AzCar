package com.project.AzCar.Entities.Cars;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbcarimages")
public class CarImages implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3271140619156877524L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String urlImage;
	
	@Transient
	@OneToMany(mappedBy = "carImages", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private int carId;
}
