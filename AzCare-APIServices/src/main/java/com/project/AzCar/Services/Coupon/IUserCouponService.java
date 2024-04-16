package com.project.AzCar.Services.Coupon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Coupon.UserCoupon;

@Service
public interface IUserCouponService {
	

	List<UserCoupon> findAllUserCoupon();
	
	UserCoupon getUserCouponByUserId(int id);
	UserCoupon getUserCouponByCouponId(int id);
	UserCoupon saveUserCoupon(UserCoupon c);
}
