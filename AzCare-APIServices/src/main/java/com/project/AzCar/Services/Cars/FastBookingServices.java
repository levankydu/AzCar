package com.project.AzCar.Services.Cars;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.FastBooking;

@Service
public interface FastBookingServices {
	void save(FastBooking model);
	FastBooking findByCarId(int id);
}
