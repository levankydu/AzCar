package com.project.AzCar.Dto.Orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Utilities.OrderExtraFee;
import com.project.AzCar.Utilities.OrderExtraFeeConverter;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
	private int id;

	private String carId;
	private CarInforDto car;
	private int userId;
	private Users user;
	private BigDecimal halfPaid;
	private BigDecimal totalRent;

	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private int differenceDate;
	private String deliveryAddress;
	private boolean isSameProvince;
	private boolean isSameDistrict;
	private BigDecimal originPrice;
	private int discount;

	@Convert(converter = OrderExtraFeeConverter.class)
	private OrderExtraFee extraFee;

	private String status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public BigDecimal getTotalAndFees() {
		BigDecimal deliFee = BigDecimal.valueOf(this.extraFee.getDeliveryFee());
		BigDecimal cleanFee = BigDecimal.valueOf(this.extraFee.getCleanFee());
		BigDecimal smellFee = BigDecimal.valueOf(this.extraFee.getSmellFee());
		BigDecimal insuranceFee = BigDecimal.valueOf(this.extraFee.getInsurance());
		return this.getTotalRent().add(deliFee).add(cleanFee).add(smellFee).add(insuranceFee);
	}

}
