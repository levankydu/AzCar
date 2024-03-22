package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.BrandImages;

@Service
public interface BrandImageServices {
	void saveImage(BrandImages model);
	void updateImage(BrandImages brand);
	String getBrandImgUrl(String brandName);
	
	List<BrandImages> getAll();
}
