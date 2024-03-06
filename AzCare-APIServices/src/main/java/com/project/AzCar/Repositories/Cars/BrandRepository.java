package com.project.AzCar.Repositories.Cars;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Dto.Brands.BrandsDto;
import com.project.AzCar.Entities.Cars.CarModelList;
@Repository
public interface BrandRepository extends JpaRepository<CarModelList, String> {

	@Query(value = "SELECT DISTINCT c.brand FROM CarModelList c")
	List<String> getBrandList(); 

	@Query("SELECT c FROM CarModelList c WHERE c.brand = ?1")
	List<CarModelList> getCarModelByBrand(String brandName);
	
	@Query(value = "SELECT DISTINCT c.category FROM CarModelList c")
	List<String> getCategoryList(); 
	
	@Query("SELECT c FROM CarModelList c WHERE c.category = ?1")
	List<CarModelList> getCarModelByCategory(String categoryName);
}
