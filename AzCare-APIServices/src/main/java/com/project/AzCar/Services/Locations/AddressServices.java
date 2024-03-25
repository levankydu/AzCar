package com.project.AzCar.Services.Locations;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.Addreess;

@Service
public interface AddressServices {
	void save(Addreess model);
}
