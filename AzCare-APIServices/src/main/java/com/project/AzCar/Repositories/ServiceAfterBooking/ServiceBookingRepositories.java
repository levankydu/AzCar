package com.project.AzCar.Repositories.ServiceAfterBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;

@Repository
public interface ServiceBookingRepositories extends JpaRepository<ServiceAfterBooking, Integer> {
	@Query(value = "SELECT c FROM ServiceAfterBooking c WHERE c.carId=?1")
	List<ServiceAfterBooking> getFromCarId(int carId);
}
