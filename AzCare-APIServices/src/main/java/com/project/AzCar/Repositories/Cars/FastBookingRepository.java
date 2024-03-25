package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.FastBooking;
@Repository
public interface FastBookingRepository extends JpaRepository<FastBooking, Integer> {

	@Query(value = "SELECT c FROM FastBooking c WHERE c.carRegisterdId =?1")
	FastBooking findByCarId(int id);
}
