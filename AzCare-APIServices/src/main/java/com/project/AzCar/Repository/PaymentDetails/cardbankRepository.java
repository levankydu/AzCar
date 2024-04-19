package com.project.AzCar.Repository.PaymentDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Deposit.Cardbank;

@Repository
public interface cardbankRepository extends JpaRepository<Cardbank, Integer> {
	@Query(value = "select * from tb_cardbank r where r.user_id = :id", nativeQuery = true)
	Cardbank findByUserId(@Param("id") long id);

}
