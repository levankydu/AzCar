package com.project.AzCar.Services.Cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Repositories.Cars.CarImagesRepositoy;
@Service
public class CarImageServiceImpl implements CarImageServices {

	
	
	@Autowired
	private CarImagesRepositoy carImagesRepositoy;
	@Override
	public void saveImg(CarImages model) {
		carImagesRepositoy.save(model);
		
	}

	
}
