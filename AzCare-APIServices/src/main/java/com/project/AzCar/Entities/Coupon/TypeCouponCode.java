package com.project.AzCar.Entities.Coupon;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "type_couponCode")
@Data
@Getter
@Setter
public class TypeCouponCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	  @Column(name = "name_type")
	private String nameType;
	
}
