package com.project.AzCar.Entities.Coupon;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Coupons")
@Data
@Getter
@Setter
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "coupon_code", unique = true, nullable = false)
	private String couponCode;
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private EnumCoupon status;

	@Column(name = "discount_percentage")
	private double discountPercentage;

	
	// ngày hết hạn
	
	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "content")
	private String Content;

	@Column(name = "quantity")
	private int quantity;
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "type_id")
	private TypeCouponCode type_couponCode;
}
