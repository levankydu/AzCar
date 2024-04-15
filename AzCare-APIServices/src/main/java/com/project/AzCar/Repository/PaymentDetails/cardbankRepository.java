package com.project.AzCar.Repository.PaymentDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Deposit.Cardbank;

@Repository
public interface cardbankRepository extends JpaRepository<Cardbank,Integer>{

}
