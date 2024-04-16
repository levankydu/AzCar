package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.BrandImages;

@Repository
public interface BrandImageRepository extends JpaRepository<BrandImages, Long> {

	@Query("SELECT c FROM BrandImages c WHERE c.brandName = ?1")
	BrandImages getBrandImg(String brandName);

}
