package com.project.AzCar.Services.Payments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.Payment;
import com.project.AzCar.Repositories.Payments.PaymentRepository;

@Service
public class PaymentServiceimpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Override
	public boolean save(Payment payment) {
		var savedPayment = paymentRepository.save(payment);
		return savedPayment != null;
	}

	@Override
	public Payment getById(int id) {
		return paymentRepository.getById(id);
	}

	@Override
	public List<Payment> getFromCreatedBy(int id) {
		return paymentRepository.getFromCreatedBy(id);
	}

}
