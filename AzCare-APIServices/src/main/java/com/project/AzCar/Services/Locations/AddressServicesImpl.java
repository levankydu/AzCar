package com.project.AzCar.Services.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.Addreess;
import com.project.AzCar.Repositories.Locations.AddressRepository;

@Service
public class AddressServicesImpl implements AddressServices {

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public void save(Addreess model) {
		addressRepository.save(model);

	}

}
