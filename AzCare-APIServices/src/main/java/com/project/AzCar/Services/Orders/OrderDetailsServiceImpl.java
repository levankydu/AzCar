package com.project.AzCar.Services.Orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Repositories.Orders.OrderRepository;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public OrderDetails getById(int id) {
		return orderRepository.getById(id);
	}

	@Override
	public List<OrderDetails> getFromCreatedBy(int id) {
		return orderRepository.getFromCreatedBy(id);
	}

	@Override
	public OrderDetails save(OrderDetails order) {
		var saveModel = orderRepository.save(order);
		return saveModel;
	}

	@Override
	public OrderDetails update(OrderDetails order) {
		var updatedModel = orderRepository.save(order);
		return updatedModel;
	}

}
