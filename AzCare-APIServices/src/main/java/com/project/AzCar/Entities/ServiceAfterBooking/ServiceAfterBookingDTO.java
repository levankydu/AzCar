package com.project.AzCar.Entities.ServiceAfterBooking;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.OrderDetails;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAfterBookingDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int carId;
	private CarInforDto car;
	private int orderId;
	private OrderDetailsDTO order;
	private String imgUrl;
	private String decriptions;
	private int paymentId;
	private String status;
	private LocalDateTime createdAt;
	private boolean isCleanning;
	private boolean isSmelling;
}
