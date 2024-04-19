package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Deposit;

@Service

public interface IDepositService {
	public Deposit findByRefenceId(String id);

	public void savePaymentDetails(Deposit d);

	public List<Deposit> findListDeposit();

	public List<Deposit> findListUserById(int id);

	public Deposit updateDeposit(Deposit c);
}
