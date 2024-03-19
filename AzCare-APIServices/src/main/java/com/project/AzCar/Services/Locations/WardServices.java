package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.Ward;

@Service
public interface WardServices {

	List<Ward> getWardByDistrictCode(String code);

	List<String> getWardListString();
}
