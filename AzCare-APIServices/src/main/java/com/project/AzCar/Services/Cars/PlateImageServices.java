package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.PlateVerify.PlateVerifyDto;
import com.project.AzCar.Entities.Cars.PlateImages;

@Service
public interface PlateImageServices {

	void save(PlateImages model);

	List<PlateImages> getAll();

	List<Long> getUserIdList();

	PlateVerifyDto maptoDto(int id);
}
