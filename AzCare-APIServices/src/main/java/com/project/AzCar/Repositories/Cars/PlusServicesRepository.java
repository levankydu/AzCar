package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.PlusServices;
@Repository
public interface PlusServicesRepository extends JpaRepository<PlusServices, Integer>{

	@Query(value = "SELECT c FROM PlusServices c WHERE c.carRegisterId =?1")
	PlusServices findByCarId(int id);
}
