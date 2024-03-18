package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarImages;

@Service
public interface CarImageServices {

	void saveImg(CarImages model);
	List<CarImages> getImgByCarId(int id);
}
