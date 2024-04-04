package com.project.AzCar.Dto.Brands;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5446745943280776453L;

	public String brandName;

	public String brandLogo =null;
	public Long numberOfCars;
	
	


}
