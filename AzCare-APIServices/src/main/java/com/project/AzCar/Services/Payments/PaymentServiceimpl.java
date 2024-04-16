package com.project.AzCar.Services.Payments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Payments.PaymentDTO;
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
	@Autowired
	private ModelMapper mapper;

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
		payment.setDescription("Admin khoa tien");
		payment.setStatus(Constants.paymentStatus.LOCKED);
		paymentRepository.save(payment);

		user.setBalance(userBalance.subtract(amount));
		admin.setBalance(adminBalance.add(amount));
		userServices.saveUserReset(admin);
		userServices.saveUserReset(user);
	}

	@Override
	public void createNewProfit(long fromUserId, BigDecimal amount, ProfitCallBack callback, boolean isDeposit) {
		Users user = userServices.findById(fromUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setUserId((int) fromUserId);
		payment.setToUserId((int) admin.getId());
		payment.setAmount(amount);
		if(isDeposit) {
			payment.setDescription("Deposit (nap tien)");
			payment.setStatus(Constants.paymentStatus.DEPOSIT);
		}else {
			payment.setDescription("Profit (Tien loi)");
			payment.setStatus(Constants.paymentStatus.PROFIT);
		}
		paymentRepository.save(payment);
		
		callback.onProcess(user, userBalance, amount);
		
		admin.setBalance(adminBalance.add(amount));
		userServices.saveUserReset(admin);
		userServices.saveUserReset(user);

	}

	@Override
	public void createNewExpense(long toUserId, BigDecimal amount, ProfitCallBack callback, boolean isWithdraw) {
		Users user = userServices.findById(toUserId);
		BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);
		var payment = new Payment();
		payment.setUserId((int) admin.getId());
		payment.setToUserId((int) toUserId);
		payment.setAmount(amount);
		if(isWithdraw) {
			payment.setDescription("Withdraw (rut tien)");
			payment.setStatus(Constants.paymentStatus.WITHDRAW);
		}else {
			payment.setDescription("Expense (Tien lo)");
			payment.setStatus(Constants.paymentStatus.EXPENSE);
		}
		paymentRepository.save(payment);
		
		callback.onProcess(user, userBalance, amount);
		
		admin.setBalance(adminBalance.subtract(amount));
		userServices.saveUserReset(admin);
		userServices.saveUserReset(user);

	}

	@Override
	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	@Override
	public List<PaymentDTO> findListDTO() {
		List<PaymentDTO> result = new ArrayList<>();
		var list = paymentRepository.findAll();

		for (var item : list) {
			PaymentDTO model = this.mapper.map(item, PaymentDTO.class);
			Users toUser = userServices.findById(item.getToUserId());
			Users fromUser = userServices.findById(item.getUserId());
			model.setFromUser(fromUser);
			model.setToUser(toUser);
			result.add(model);
		}
		return result;
	}

	@Override
	public List<String> getDayStringFomart() {
		 List<String> result = new ArrayList<>();
		    List<Payment> list = paymentRepository.findAll();
		    for (var item : list) {
		        result.add(item.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		    }
		    Collections.sort(result);
		    Set<String> setWithoutDuplicates = new LinkedHashSet<String>(result);
		    return new ArrayList<>(setWithoutDuplicates);
	}

	@Override
	public List<PaymentDTO> getPaymentByDate(LocalDate date) {
		List<PaymentDTO> result = new ArrayList<>();
		List<PaymentDTO> list = findListDTO();
		for (var item : list) {
			if (item.getCreatedAt().toLocalDate().isEqual(date)) {
				result.add(item);
			}
		}
		return result;
	}

}
