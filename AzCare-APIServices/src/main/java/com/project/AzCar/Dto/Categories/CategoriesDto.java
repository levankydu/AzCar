package com.project.AzCar.Dto.Categories;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809198163623824301L;

	public String cateName;

	public Long numberOfCars;

}
