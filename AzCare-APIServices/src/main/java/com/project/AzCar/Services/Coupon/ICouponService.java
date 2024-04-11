package com.project.AzCar.Services.Coupon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Coupon.Coupon;

@Service
public interface ICouponService {
	public Coupon getCouponById(int id);

	public Coupon updateCouponByid(int id);

	public void deleteCouponById(int id);

	public List<Coupon> getListCoupon();

	public Coupon createCoupon(Coupon c);

}
