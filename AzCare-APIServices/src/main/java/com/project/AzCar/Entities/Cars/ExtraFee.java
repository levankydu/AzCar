package com.project.AzCar.Entities.Cars;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbextrafee")
public class ExtraFee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6591198605083296869L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long cleanningFee;
	private long decorationFee;
	private int carRegisterId;
	
}
