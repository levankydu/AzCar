package com.project.AzCar.Entities.Cars;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbpayments")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int userId;

	private int toUserId;

	private Integer orderDetailsId;

	private BigDecimal amount;

	private String description;

	private String status;

	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public Payment(int userId, int toUserId, Integer orderDetailsId, BigDecimal amount, String description,
			String status) {
		super();
		this.userId = userId;
		this.toUserId = toUserId;
		this.orderDetailsId = orderDetailsId;
		this.amount = amount;
		this.description = description;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}
}