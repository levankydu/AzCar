package com.project.AzCar.Services.Cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Repositories.Cars.CarRepository;

@Service
public class CarServicesImpl implements CarServices{

	@Autowired
	private CarRepository carRepository;
	@Override
	public void saveCarRegister(CarInfor model) {
		carRepository.save(model);
		
	}

}
