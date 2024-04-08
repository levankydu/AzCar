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
@Table(name = "tbplusservices")
public class PlusServices implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -57193474630692460L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private long fee;

	private int carRegisterId;

}
