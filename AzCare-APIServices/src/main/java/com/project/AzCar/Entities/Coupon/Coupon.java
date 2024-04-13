package com.project.AzCar.Entities.Coupon;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Coupons")
@Data
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "coupon_code", unique = true, nullable = false)
	private String couponCode;
	@Column(name = "status")
	private EnumCoupon status;

	@Column(name = "discount_percentage")
	private double discountPercentage;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "content")
	private String Content;

	@Column(name = "quantity")
	private int quantity;
}
