package com.project.AzCar.Dto.PaymentDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class PaymentDetailsDTO {
	private int id;
	private String Status;
	private String referenceNumber;
	private long userId;
	private BigDecimal amount;
	private String email;
	private BigDecimal withdraw;
	private String timeCreated;
}
