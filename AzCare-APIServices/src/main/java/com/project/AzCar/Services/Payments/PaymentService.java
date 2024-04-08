package com.project.AzCar.Services.Payments;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.Payment;

@Service
public interface PaymentService {
	boolean save(Payment payment);

	Payment getById(int id);

	List<Payment> getFromCreatedBy(int id);

	void createNewRefund(long toUserId, int orderId, BigDecimal amount);

	void createNewLock(long fromUserId, int orderId, BigDecimal amount);

	void createNewDeposit(long toUserId, BigDecimal amount);

	void createNewWithdraw(long fromUserId, BigDecimal amount);
}