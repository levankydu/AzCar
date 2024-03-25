package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Repositories.Locations.DistrictRepository;

@Service
public class DistrictServiceImpl implements DistrictServices{

	
	@Autowired
	private DistrictRepository districtRepository;
	@Override
	public List<District> getDistricByProvinceCode(String code) {
		return districtRepository.getDistrictByProviceCode(code);
	}
	@Override
	public List<String> getListDistrictString() {
		return districtRepository.getListString();
	}

}
