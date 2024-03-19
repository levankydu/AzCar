package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarInfor;

@Service
public interface CarServices {
	void saveCarRegister(CarInfor model);
	List<CarInfor> findAll();
	CarInforDto mapToDto(int id);
	CarInfor findById(int id);
}
