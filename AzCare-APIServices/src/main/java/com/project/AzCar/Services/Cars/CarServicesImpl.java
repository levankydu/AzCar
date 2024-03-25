package com.project.AzCar.Services.Cars;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Repositories.Cars.CarRepository;

@Service
public class CarServicesImpl implements CarServices {

	@Autowired
	private CarRepository carRepository;

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

}
