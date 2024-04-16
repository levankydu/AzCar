package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBookingDTO;

@Service
public interface CarServices {
	void saveCarRegister(CarInfor model);

	List<CarInfor> findAll();

	ServiceAfterBookingDTO afterServiceMapToDto(int id);

	CarInforDto mapToDto(int id);

	CarInfor findById(int id);

	List<CarInfor> getbyOwnerId(int id);
}
