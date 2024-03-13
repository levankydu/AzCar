package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.City;

@Service
public interface ProvinceServices {
 List<City> getListCity();
}
