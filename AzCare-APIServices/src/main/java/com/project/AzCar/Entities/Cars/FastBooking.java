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
@Table(name="tbfastbooking")
public class FastBooking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1676654435517427978L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private long fee;
	
	private long estimateTime;
	
	private int carRegisterdId;
	
	
}
