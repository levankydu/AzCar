package com.project.AzCar.Services.Orders;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Users.Users;

import jakarta.mail.MessagingException;

@Service
public interface OrderDetailsService {
	OrderDetails save(OrderDetails order);

	OrderDetails update(OrderDetails order);

	OrderDetails getById(int id);

	List<OrderDetails> getFromCreatedBy(int id);

	List<OrderDetails> getFromCarId(int carId);

	List<OrderDetailsDTO> getDTOFromCreatedBy(int id);

	void unrespondDetected(Users currentUser);

	List<OrderDetailsDTO> getDTOFromCarId(int id);

	// Sally add
	OrderDetails getOrderDetailsByCarIdandUserId(long carId, long userId);

	OrderDetails getRentorTripDoneOrder();

	OrderDetailsDTO getDTORentorTripDoneOrder();

	OrderDetailsDTO mapToDTO(int id);

	void sendOrderEmail(String email, String subject, String content) throws UnsupportedEncodingException, MessagingException;

}
