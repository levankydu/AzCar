package com.project.AzCar.Services.Cars;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.PlateVerify.PlateVerifyDto;
import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Repositories.Cars.PlateImagesRepository;

@Service
public class PlateImageServicesImpl implements PlateImageServices {

	@Autowired
	private PlateImagesRepository plateImagesRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void save(PlateImages model) {
		plateImagesRepository.save(model);

	}

	@Override
	public List<PlateImages> getAll() {
		return plateImagesRepository.findAll();
	}

	@Override
	public List<Long> getUserIdList() {
		return plateImagesRepository.listUserId();
	}

	@Override
	public PlateVerifyDto maptoDto(int id) {
		PlateImages raw = this.plateImagesRepository.findById(id).get();
		PlateVerifyDto Dto = this.modelMapper.map(raw, PlateVerifyDto.class);
		return Dto;
	}

}
