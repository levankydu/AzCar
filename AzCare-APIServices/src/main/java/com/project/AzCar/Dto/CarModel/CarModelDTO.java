package com.project.AzCar.Dto.CarModel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarModelDTO {

	@Id
	private String objectId;

	private String brand;
	private String category;

	private String model;

	private Long year;
	private String status;
	private String emailSender;
}
