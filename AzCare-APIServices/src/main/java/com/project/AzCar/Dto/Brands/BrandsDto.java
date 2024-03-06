package com.project.AzCar.Dto.Brands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.project.AzCar.Entities.Cars.BrandImages;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
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
