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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbplateimages")
public class PlateImages implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2249659101044324722L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String ImageUrl = null;
	private long userId;
	private String status;
}
