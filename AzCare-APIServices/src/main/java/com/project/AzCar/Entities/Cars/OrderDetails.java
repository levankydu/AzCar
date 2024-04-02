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
	
	private BigDecimal halfPaid;
	private BigDecimal totalRent;
	
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private int differenceDate;
	private String deliveryAddress;
	private boolean isSameProvince;
	private boolean isSameDistrict;
	
	@Convert(converter = OrderExtraFeeConverter.class)
    private OrderExtraFee extraFee;
	
	private String status;
}
