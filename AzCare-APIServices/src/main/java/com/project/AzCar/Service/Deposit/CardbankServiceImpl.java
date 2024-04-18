package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Repository.PaymentDetails.cardbankRepository;

@Service

public class CardbankServiceImpl implements ICarbankService {
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

	@Override
	public Cardbank findCardbankByUserId(int id) {
		// TODO Auto-generated method stub
		return cardRepo.findByUserId(id);
	}

	@Override
	public List<Cardbank> getListCardBankAdmin() {
		List<Cardbank> cards = cardRepo.findAll();
		cards.removeIf(i -> i.getUser() != null);
		return cards;
	}
}
