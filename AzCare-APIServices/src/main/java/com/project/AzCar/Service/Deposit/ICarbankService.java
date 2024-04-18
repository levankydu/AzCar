package com.project.AzCar.Service.Deposit;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Deposit.Cardbank;

@Service
public interface ICarbankService {
	List<Cardbank> getListCardBank();

<<<<<<< HEAD
	List<Cardbank> getListCardBankAdmin();

=======
>>>>>>> 46f90fc3fb8dc50ff5e40fa667557365d0ae2229
	Cardbank saveCardbank(Cardbank c);

	Cardbank findCardbankbyId(int id);

	Cardbank findCardbankByUserId(int id);
}
