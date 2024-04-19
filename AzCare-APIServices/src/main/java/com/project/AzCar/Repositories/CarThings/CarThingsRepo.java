package com.project.AzCar.Repositories.CarThings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.CarThings.FavoriteCar;

@Repository
public interface CarThingsRepo extends JpaRepository<FavoriteCar, Integer> {
	@Query(value = "SELECT c FROM FavoriteCar c WHERE c.userId=?1")
	List<FavoriteCar> getFromCreatedBy(int id);

	@Query(value = "SELECT c FROM FavoriteCar c WHERE c.carId=?1")
	List<FavoriteCar> getFromCarId(int id);

	@Query(value = "SELECT c FROM FavoriteCar c WHERE c.carId=?1 AND c.userId=?2")
	FavoriteCar getByCarAndUser(int carId, int userId);
}
