package com.project.AzCar.Repositories.Cars;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.PlateImages;

@Repository
public interface PlateImagesRepository extends JpaRepository<PlateImages, Integer> {

	@Query(value = "SELECT DISTINCT c.userId FROM PlateImages c")
	List<Long> listUserId();

}
