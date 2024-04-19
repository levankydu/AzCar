package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Cardbank;

@Service
public interface ICarbankService {
	List<Cardbank> getListCardBank();

	List<Cardbank> getListCardBankAdmin();

	Cardbank saveCardbank(Cardbank c);

	Cardbank findCardbankbyId(int id);

	Cardbank findCardbankByUserId(long id);

}