package com.project.AzCar.Repositories.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Coupon.UserCoupon;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

	@Query(value = "select * from user_coupons r where r.user_id = :id", nativeQuery = true)
	UserCoupon findUserCouponByUserId(@Param("id") int id);

	@Query(value = "select * from user_coupons r where r.coupon_id = :id", nativeQuery = true)
	UserCoupon findUserCouponByCouponId(@Param("id") int id);
}
