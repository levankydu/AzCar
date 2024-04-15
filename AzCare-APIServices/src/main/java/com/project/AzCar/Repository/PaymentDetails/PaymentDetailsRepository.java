package com.project.AzCar.Repository.PaymentDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Deposit.Deposit;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<Deposit,Integer>{

	@Query(value ="select * from tb_deposit r where r.reference_number = :id",nativeQuery = true)
	public Deposit findbyreference_number(@Param("id") String id);
}
