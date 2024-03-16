package com.project.AzCar.Services.Cars;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarImages;

@Service
public interface CarImageServices {

	void saveImg(CarImages model);
	
}
