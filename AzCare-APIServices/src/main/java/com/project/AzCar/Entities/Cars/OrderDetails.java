package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.AzCar.Utilities.OrderExtraFeeConverter;
import com.project.AzCar.Utilities.OrderExtraFee;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tborderdetails")
public class OrderDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String carId;
	private int userId;

	private BigDecimal totalRent;

	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private int differenceDate;
	private String deliveryAddress;
	private boolean isSameProvince;
	private boolean isSameDistrict;
	private boolean isReview = false;
	private BigDecimal originPrice;
	private int discount;
	@Convert(converter = OrderExtraFeeConverter.class)
	private OrderExtraFee extraFee;

	private String status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public BigDecimal getTotalAndFees() {
		BigDecimal deliFee = BigDecimal.valueOf(this.extraFee.getDeliveryFee());
		BigDecimal cleanFee = BigDecimal.valueOf(this.extraFee.getCleanFee());
		BigDecimal smellFee = BigDecimal.valueOf(this.extraFee.getSmellFee());
		BigDecimal insuranceFee = BigDecimal.valueOf(this.extraFee.getInsurance());
		return this.getTotalRent().add(deliFee).add(cleanFee).add(smellFee).add(insuranceFee);
	}
}
