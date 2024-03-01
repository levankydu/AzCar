package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.AzCar.Entities.Cars.CarInfor;

public interface CarRepository extends JpaRepository<CarInfor,Integer>{

}
