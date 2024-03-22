package com.project.AzCar.Repositories.Cars;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.CarInfor;

@Repository
public interface CarRepository extends JpaRepository<CarInfor,Integer>{

	@Query(value = "SELECT DISTINCT c FROM CarInfor c WHERE c.id=?1")
	CarInfor getById(int id);
	
	@Query(value = "SELECT c FROM CarInfor c WHERE c.carOwnerId=?1")
	List<CarInfor> getbyOwnerId(int id);
}
