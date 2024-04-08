package com.project.AzCar.Repositories.Payments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.AzCar.Entities.Cars.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	@Query(value = "SELECT c FROM Payment c WHERE c.id=?1")
	Payment getById(int id);

	@Query(value = "SELECT c FROM Payment c WHERE c.userId=?1")
	List<Payment> getFromCreatedBy(int id);
}