package com.project.AzCar.Repositories.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Coupon.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	@Query(value="select * from coupons c where c.coupon_code = :coupon_code" ,nativeQuery = true)
	Coupon findCouponByCouponCode(@Param("coupon_code") String code);
	@Query(value="select * from coupons c where c.type_id = :id" ,nativeQuery = true)
	Coupon findCouponByTypeCoupon(@Param("id") int code);

}
