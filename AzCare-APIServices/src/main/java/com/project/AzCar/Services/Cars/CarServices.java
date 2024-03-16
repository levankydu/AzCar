package com.project.AzCar.Services.Cars;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarInfor;

@Service
public interface CarServices {
	void saveCarRegister(CarInfor model);
}
