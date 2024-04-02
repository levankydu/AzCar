package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.District;

@Service
public interface DistrictServices {

	List<District> getDistricByProvinceCode(String code);

	List<String> getListDistrictString();
	District findbyId(String id);

}
