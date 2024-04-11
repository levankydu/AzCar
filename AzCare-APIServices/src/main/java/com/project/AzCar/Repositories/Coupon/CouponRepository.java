package com.project.AzCar.Repositories.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Coupon.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

}
