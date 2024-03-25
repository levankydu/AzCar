package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Repositories.Locations.WardRepository;

@Service
public class WardServiceImpl implements WardServices {

	@Autowired
	private WardRepository wardRepository;
	@Override
	public List<Ward> getWardByDistrictCode(String code) {
		return wardRepository.getWardByDistrictCode(code);
	}
	@Override
	public List<String> getWardListString() {
		return wardRepository.getListString();
	}

}
