package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarModelList;
import com.project.AzCar.Repositories.Cars.BrandRepository;

@Service
public class BrandSevicesImpl implements BrandServices {

	@Autowired
	private BrandRepository brandRepo;

	@Override
	public void saveBrand(CarModelList model) {
		brandRepo.save(model);

	}

	@Override
	public void updateBrand(String idCarModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getBrandList() {

		return brandRepo.getBrandList();
	}

	@Override
	public List<CarModelList> getCarsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CarModelList> getCarsListByBrand(String brandName) {
		return brandRepo.getCarModelByBrand(brandName);
	}

	@Override
	public List<CarModelList> getCarsListByCategory(String categoryName) {
		return brandRepo.getCarModelByCategory(categoryName);
	}

	@Override
	public List<String> getCategoryList() {
		return brandRepo.getCategoryList();
	}

	@Override
	public List<String> getCategoryListByBrand(String brandName) {
		return brandRepo.getCategoryListByBrand(brandName);
	}

	@Override
	public List<String> getModelListByCateAndBrand(String brandName, String cateName) {
		return brandRepo.getModelListByCateAndBrand(brandName, cateName);
	}

	@Override
	public List<String> getYear(String brandName, String cateName, String modelName) {
		return brandRepo.getYearList(brandName, cateName, modelName);
	}

	@Override
	public String getModelId(String brandName, String cateName, String modelName, int year) {
		return brandRepo.getModelId(brandName, cateName, modelName, year);
	}

	@Override
	public CarModelList getModel(String modelId) {
		return brandRepo.getModelById(modelId);
	}

}
