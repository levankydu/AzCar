package com.project.AzCar.Repositories.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Cars.FastBooking;
@Repository
public interface FastBookingRepository extends JpaRepository<FastBooking, Integer> {

}
