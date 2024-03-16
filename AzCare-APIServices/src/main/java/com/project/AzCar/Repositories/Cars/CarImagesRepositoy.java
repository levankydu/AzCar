package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.CarImages;
@Repository
public interface CarImagesRepositoy extends JpaRepository<CarImages, Long> {

	
	
}
