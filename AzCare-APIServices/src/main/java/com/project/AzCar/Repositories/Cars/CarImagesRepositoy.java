package com.project.AzCar.Repositories.Cars;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.CarImages;

@Repository
public interface CarImagesRepositoy extends JpaRepository<CarImages, Long> {

	@Query(value = "SELECT c from CarImages c WHERE c.carId=?1")
	List<CarImages> getImgByCarId(int id);

}
