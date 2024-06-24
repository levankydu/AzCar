package com.project.AzCar.Repositories.Cars;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.CarModelList;

@Repository
public interface BrandRepository extends JpaRepository<CarModelList, String> {

	@Query(value = "SELECT DISTINCT c.brand FROM CarModelList c WHERE c.status ='accepted' OR c.status IS NULL ")
	List<String> getBrandList();

	@Query("SELECT c FROM CarModelList c WHERE c.brand = ?1")
	List<CarModelList> getCarModelByBrand(String brandName);

	@Query(value = "SELECT DISTINCT c.category FROM CarModelList c")
	List<String> getCategoryList();

	@Query("SELECT c FROM CarModelList c WHERE c.category = ?1")
	List<CarModelList> getCarModelByCategory(String categoryName);

	@Query("SELECT DISTINCT c.category FROM CarModelList c WHERE c.brand = ?1")
	List<String> getCategoryListByBrand(String brandName);

	@Query("SELECT DISTINCT c.model FROM CarModelList c WHERE c.brand = ?1 AND c.category =?2")
	List<String> getModelListByCateAndBrand(String brandName, String cateName);

	@Query("SELECT DISTINCT c.year FROM CarModelList c WHERE c.brand = ?1 AND c.category = ?2 AND c.model = ?3")
	List<String> getYearList(String brandName, String cateName, String modelName);

	@Query("SELECT DISTINCT c.objectId FROM CarModelList c WHERE c.brand = ?1 AND c.category = ?2 AND c.model = ?3 AND c.year= ?4")
	String getModelId(String brandName, String cateName, String modelName, int year);

	@Query("SELECT DISTINCT c FROM CarModelList c WHERE c.objectId = ?1")
	CarModelList getModelById(String id);
}
