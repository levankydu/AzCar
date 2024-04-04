package com.project.AzCar.Repositories.Orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.OrderDetails;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails, Integer> {
	@Query(value = "SELECT c FROM OrderDetails c WHERE c.id=?1")
	OrderDetails getById(int id);
	
	@Query(value = "SELECT c FROM OrderDetails c WHERE c.userId=?1")
	List<OrderDetails> getFromCreatedBy(int id);
	
	@Query(value = "SELECT c FROM OrderDetails c WHERE c.carId=?1")
	List<OrderDetails> getFromCarId(int carId);

	@Query(value = "SELECT c FROM OrderDetails c")
	List<OrderDetails> getAll();
}
