package com.project.AzCar.Services.Locations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Repositories.Locations.ProvinceRepository;

@Service
public class ProvinceServiceImpl implements ProvinceServices{

	
	
	@Autowired
	private ProvinceRepository proviceRepository;
	@Override
	public List<City> getListCity() {
		return proviceRepository.findAll();
	}
	@Override
	public List<String> getListCityString() {
		return proviceRepository.getListString();
	}
	@Override
	public City findById(String id) {
		return proviceRepository.findbyId(id);
	}
	@Override
	public City findByCode(String name) {
		return proviceRepository.findbyCode(name);
	}

}
