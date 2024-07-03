package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Deposit;
import com.project.AzCar.Repository.PaymentDetails.PaymentDetailsRepository;

@Service
public class DepositServiceImpl implements IDepositService {
	@Autowired
	private PaymentDetailsRepository paymentDetailsRepository;

	@Override
	public void savePaymentDetails(Deposit d) {
		// TODO Auto-generated method stub

		paymentDetailsRepository.save(d);
	}

	@Override
	public Deposit findByRefenceId(String id) {
		// TODO Auto-generated method stub
		return paymentDetailsRepository.findbyreference_number(id);
	}

	@Override
	public List<Deposit> findListDeposit() {
		// TODO Auto-generated method stub

		return paymentDetailsRepository.findAll();
	}

	@Override
	public Deposit updateDeposit(Deposit c) {
		// TODO Auto-generated method stub
		return paymentDetailsRepository.save(c);
	}

	@Override
	public List<Deposit> findListUserById(int id) {
		return paymentDetailsRepository.findListUserById(id);
	}

	@Override
	public List<Deposit> findListDepositWithDraw() {
		// TODO Auto-generated method stub
		return paymentDetailsRepository.findListWithDraw();
	}

	@Override
	public List<Deposit> findListDepositTransaction() {
		// TODO Auto-generated method stub
		return paymentDetailsRepository.findListTransaction();
	}

	@Override
	public String removeDeposit(Deposit c) {
		paymentDetailsRepository.delete(c);
		return "Deposit had been deleted";
	}

}
