package com.project.AzCar.Repositories.Orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Users.Violation;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Integer> {
	@Query(value = "SELECT c FROM Violation c WHERE c.id=?1")
	Violation getById(int id);

	@Query(value = "SELECT c FROM Violation c WHERE c.userId=?1")
	List<Violation> getByUserId(long id);

	@Query(value = "SELECT c FROM Violation c WHERE c.carId=?1")
	List<Violation> getByCarId(int id);
	
	@Query(value = "SELECT c FROM Violation c WHERE c.carId=?1 AND c.isEnabled = true")
	List<Violation> getEnabledByCarId(int id);

	@Query(value = "SELECT c FROM Violation c WHERE c.userId=?1 AND c.carId=?2 AND c.isEnabled=?3 AND c.reason=?4")
	List<Violation> getByUserAndCarId(long userId, int carId, boolean isEnabled, String reason);

	@Query(value = "SELECT c FROM Violation c")
	List<Violation> getAll();
}
