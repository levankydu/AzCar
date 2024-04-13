package com.project.AzCar.Services.Payments;

import java.math.BigDecimal;

import com.project.AzCar.Entities.Users.Users;

public interface ProfitCallBack {
	 void onProcess(Users user, BigDecimal userBalance ,BigDecimal amount);
}
