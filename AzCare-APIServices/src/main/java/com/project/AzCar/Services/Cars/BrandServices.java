package com.project.AzCar.Services.Cars;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarModelList;

@Service
public interface BrandServices {

	void saveBrand(CarModelList model);

	void updateBrand(String idCarModel);

	List<CarModelList> getCarsList();

	List<String> getBrandList();

	List<String> getCategoryList();

	List<String> getCategoryListByBrand(String brandName);

	List<CarModelList> getCarsListByBrand(String brandName);

	List<CarModelList> getCarsListByCategory(String categoryName);

	List<String> getModelListByCateAndBrand(String brandName, String cateName);

	List<String> getYear(String brandName, String cateName, String modelName);

	String getModelId(String brandName, String cateName, String modelName, int year);

	CarModelList getModel(String modelId);

	List<CarModelList> findAll();

}
