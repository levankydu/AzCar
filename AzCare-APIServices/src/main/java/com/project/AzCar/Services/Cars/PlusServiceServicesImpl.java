package com.project.AzCar.Services.Cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Repositories.Cars.PlusServicesRepository;

@Service
public class PlusServiceServicesImpl implements PlusServiceServices {

	@Autowired
	private PlusServicesRepository plusServicesRepository;

	@Override
	public void save(PlusServices model) {
		plusServicesRepository.save(model);

	}

	@Override
	public PlusServices findByCarId(int id) {
		return plusServicesRepository.findByCarId(id);
	}

}
