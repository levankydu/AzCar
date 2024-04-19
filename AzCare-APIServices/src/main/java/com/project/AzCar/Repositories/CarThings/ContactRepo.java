package com.project.AzCar.Repositories.CarThings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.CarThings.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {
	@Query(value = "SELECT c FROM Contact c WHERE c.userId=?1")
	List<Contact> getFromCreatedBy(int id);

	@Query(value = "SELECT c FROM Contact c WHERE c.carId=?1")
	List<Contact> getByCarId(int id);
}
