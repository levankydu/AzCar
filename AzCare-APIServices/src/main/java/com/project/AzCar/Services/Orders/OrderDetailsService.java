package com.project.AzCar.Services.Orders;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.OrderDetails;

@Service
public interface OrderDetailsService {
	OrderDetails save(OrderDetails order);
	OrderDetails update(OrderDetails order);
	OrderDetails getById(int id);
	List<OrderDetails> getFromCreatedBy(int id);
}
