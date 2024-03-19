package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.ExtraFee;

@Repository
public interface ExtraFeeRepository extends JpaRepository<ExtraFee, Integer>{

}
