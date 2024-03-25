package com.project.AzCar.Services.Cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Repositories.Cars.ExtraFeeRepository;

@Service
public class ExtraFeeServiceImpl implements ExtraFeeServices {
	@Autowired
	private ExtraFeeRepository extraFeeRepository;

	@Override
	public void save(ExtraFee model) {
		extraFeeRepository.save(model);
	}

	@Override
	public ExtraFee findByCarId(int id) {
		return extraFeeRepository.findByCarId(id);
	}
}
