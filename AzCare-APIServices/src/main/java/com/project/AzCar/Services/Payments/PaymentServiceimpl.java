package com.project.AzCar.Services.Payments;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.Payment;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Payments.PaymentRepository;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentServiceimpl implements PaymentService {
	private Users admin;
	private BigDecimal adminBalance;

	@Autowired
	private UserServices userServices;
	@Autowired
	private PaymentRepository paymentRepository;

	@PostConstruct
	public void init() {
		this.admin = userServices.findUserByEmail("admin@admin");
		this.adminBalance = admin.getBalance() != null ? admin.getBalance() : BigDecimal.valueOf(0);
	}

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

	@Override
	public void createNewRefund(long toUserId, int orderId, BigDecimal amount) {
		Users user = userServices.findById(toUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setUserId((int) admin.getId());
		payment.setToUserId((int) toUserId);
		payment.setAmount(amount);
		payment.setOrderDetailsId(orderId);
		payment.setDescription("Admin refund tien bi khoa");
		payment.setStatus(Constants.paymentStatus.REFUND);
		paymentRepository.save(payment);

		user.setBalance(userBalance.add(amount));
		admin.setBalance(adminBalance.subtract(amount));
		userServices.saveUserReset(admin);
		userServices.saveUserReset(user);
	}

	@Override
	public void createNewLock(long fromUserId, int orderId, BigDecimal amount) {
		Users user = userServices.findById(fromUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setUserId((int) fromUserId);
		payment.setToUserId((int) admin.getId());
		payment.setAmount(amount);
		payment.setOrderDetailsId(orderId);
		payment.setDescription("Admin khoa tien thue xe");
		payment.setStatus(Constants.paymentStatus.LOCKED);
		paymentRepository.save(payment);

		user.setBalance(userBalance.subtract(amount));
		admin.setBalance(adminBalance.add(amount));
		userServices.saveUserReset(admin);
		userServices.saveUserReset(user);
	}

	@Override
	public void createNewDeposit(long toUserId, BigDecimal amount) {
		Users user = userServices.findById(toUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setToUserId((int) toUserId);
		payment.setAmount(amount);
		payment.setDescription("User nap tien");
		payment.setStatus(Constants.paymentStatus.DEPOSIT);
		paymentRepository.save(payment);

		user.setBalance(userBalance.add(amount));
		userServices.saveUserReset(user);

	}

	@Override
	public void createNewWithdraw(long fromUserId, BigDecimal amount) {
		Users user = userServices.findById(fromUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setToUserId((int) fromUserId);
		payment.setAmount(amount);
		payment.setDescription("User rut tien");
		payment.setStatus(Constants.paymentStatus.DEPOSIT);
		paymentRepository.save(payment);

		user.setBalance(userBalance.subtract(amount));
		userServices.saveUserReset(user);

	}

}