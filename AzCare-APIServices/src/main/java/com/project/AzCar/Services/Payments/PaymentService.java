package com.project.AzCar.Services.Payments;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.Payment;

@Service
public interface PaymentService {
	boolean save(Payment payment);
	Payment getById(int id);
	List<Payment> getFromCreatedBy(int id);
}
