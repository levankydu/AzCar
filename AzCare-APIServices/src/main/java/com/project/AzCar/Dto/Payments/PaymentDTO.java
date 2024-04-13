package com.project.AzCar.Dto.Payments;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.AzCar.Entities.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int userId;
	private int toUserId;
	private Integer orderDetailsId;
	private BigDecimal amount;
	private String description;
	private String status;
	private LocalDateTime createdAt;
	private Users toUser;
	private Users fromUser;
}
