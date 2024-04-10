package com.project.AzCar.Services.Cars;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBookingDTO;
import com.project.AzCar.Repositories.Cars.CarRepository;
import com.project.AzCar.Repositories.ServiceAfterBooking.ServiceBookingRepositories;

@Service
public class CarServicesImpl implements CarServices {

	@Autowired
	private CarRepository carRepository;
	@Autowired
	private ServiceBookingRepositories afterBookingRepositories;
	@Autowired
	private BrandServices brandServices;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void saveCarRegister(CarInfor model) {
		carRepository.save(model);

	}

	@Override
	public List<CarInfor> findAll() {
		return carRepository.findAll();
	}

	@Override
	public CarInforDto mapToDto(int id) {
		CarInfor car = this.carRepository.findById(id).get();
		CarInforDto carDto = this.modelMapper.map(car, CarInforDto.class);
		carDto.setCarmodel(brandServices.getModel(car.getModelId()));
		return carDto;
	}

	@Override
	public CarInfor findById(int id) {
		return carRepository.getById(id);
	}

	@Override
	public List<CarInfor> getbyOwnerId(int id) {
		return carRepository.getbyOwnerId(id);
	}
	
	@Override
	public ServiceAfterBookingDTO afterServiceMapToDto(int id) {
		ServiceAfterBooking car = this.afterBookingRepositories.findById(id).get();
		ServiceAfterBookingDTO carDto = this.modelMapper.map(car, ServiceAfterBookingDTO.class);
		return carDto;
	}

}
