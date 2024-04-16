package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Repository.PaymentDetails.cardbankRepository;

@Service

public class CardbankServiceImpl implements ICarbankService{
 @Autowired
 private cardbankRepository cardRepo;
	@Override
	public List<Cardbank> getListCardBank() {
		// TODO Auto-generated method stub
		return cardRepo.findAll();
	}

	@Override
	public Cardbank saveCardbank(Cardbank c) {
		// TODO Auto-generated method stub
		return cardRepo.save(c);
	}

	@Override
	public Cardbank findCardbankbyId(int id) {
		// TODO Auto-generated method stub
		return cardRepo.findById(id).get();
	}

}
