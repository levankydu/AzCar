package com.project.AzCar.Services.Coupon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Coupon.UserCoupon;
import com.project.AzCar.Repositories.Coupon.UserCouponRepository;

@Service
public class UserCouponServiceImpl  implements IUserCouponService{
	@Autowired
	private UserCouponRepository UcouponRepo;

	@Override
	public List<UserCoupon> findAllUserCoupon() {
		// TODO Auto-generated method stub
		return UcouponRepo.findAll();
	}

	@Override
	public UserCoupon getUserCouponByUserId(int id) {
		// TODO Auto-generated method stub
		return UcouponRepo.findUserCouponByUserId(id);
	}

	@Override
	public UserCoupon getUserCouponByCouponId(int id) {
		// TODO Auto-generated method stub
		return UcouponRepo.findUserCouponByCouponId(id);
	}

	@Override
	public UserCoupon saveUserCoupon(UserCoupon c) {
		// TODO Auto-generated method stub
		return UcouponRepo.save(c);
	}

}
