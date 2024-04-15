package com.project.AzCar.Entities.Deposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Users.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="tb_cardbank")
public class Cardbank {

	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String bankName;
	private String bankNumber;
	private String beneficiaryName;
	private String addressbank;
	
	// chổ này mình set thẻ nếu mà thẻ active thì cho phép chuyển qua còn ko thì nghỉ
	private EnumCoupon active;
	
	
}