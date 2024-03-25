package com.project.AzCar.Services.Cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.FastBooking;
import com.project.AzCar.Repositories.Cars.FastBookingRepository;

@Service
public class FastBookingServicesImpl implements FastBookingServices{

	@Autowired
	private FastBookingRepository bookingRepository;
	@Override
	public void save(FastBooking model) {
		bookingRepository.save(model);
		
	}
	@Override
	public FastBooking findByCarId(int id) {
		return bookingRepository.findByCarId(id);	}

	
}
