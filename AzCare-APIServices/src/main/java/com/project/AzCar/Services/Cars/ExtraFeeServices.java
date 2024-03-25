package com.project.AzCar.Services.Cars;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.ExtraFee;

@Service
public interface ExtraFeeServices {
	void save(ExtraFee model);
	ExtraFee findByCarId(int id);
}
