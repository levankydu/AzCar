package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.BrandImages;
import com.project.AzCar.Repositories.Cars.BrandImageRepository;

@Service
public class BrandImageServicesImpl implements BrandImageServices{

	@Autowired
	private BrandImageRepository brandImgRepository;
	@Override
	public void saveImage(BrandImages model) {
		brandImgRepository.save(model);
		
	}

	@Override
	public void updateImage(BrandImages brand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBrandImgUrl(String brandName) {
		if(brandImgRepository.getBrandImg(brandName)!=null) {
			return brandImgRepository.getBrandImg(brandName).getImageUrl();
		}
		else {
			return "null";
		}
	}

	@Override
	public List<BrandImages> getAll() {
		return brandImgRepository.findAll();
	}

}
