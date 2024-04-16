package com.project.AzCar.Entities.Deposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Users.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="tb_deposit")
public class Deposit {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	  @ManyToOne
	    @JoinColumn(name = "user_id")
	    private Users user;
	
	private BigDecimal amount;
	
	
	private LocalDateTime paymentDateAt;
	
	
	 private String referenceNumber; // Số tham chiếu
	
	private EnumDeposit status;

}
