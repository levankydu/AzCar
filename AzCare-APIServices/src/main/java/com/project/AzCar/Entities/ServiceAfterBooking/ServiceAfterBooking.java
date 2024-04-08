package com.project.AzCar.Entities.ServiceAfterBooking;

import java.io.Serializable;
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
@Table(name="tbafterbooking")
public class ServiceAfterBooking implements Serializable{

	private static final long serialVersionUID = 6359437150901778703L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int carId;
	private int orderId;
	private String imgUrl;
	private String decriptions;
	private int paymentId;
	private String status;
	private LocalDateTime createdAt;
	private boolean isCleanning;
	private boolean isSmelling;
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		status ="waiting_for_verify";
	}

}
