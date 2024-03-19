package com.project.AzCar.Services.Cars;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.PlusServices;
@Service
public interface PlusServiceServices {
	void save(PlusServices model);
	PlusServices findByCarId(int id);
}
