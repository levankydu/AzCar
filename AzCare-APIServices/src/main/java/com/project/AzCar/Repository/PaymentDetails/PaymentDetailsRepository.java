package com.project.AzCar.Repository.PaymentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Deposit.Deposit;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<Deposit, Integer> {

	@Query(value = "select * from tb_deposit r where r.reference_number = :id", nativeQuery = true)
	public Deposit findbyreference_number(@Param("id") String id);

	@Query(value = "SELECT d FROM Deposit d WHERE d.user.id = :id", nativeQuery = true)
	List<Deposit> findListUserById(@Param("id") int id);
}
