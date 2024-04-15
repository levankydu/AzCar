package com.project.AzCar.Services.Coupon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Coupon.Coupon;
import com.project.AzCar.Repositories.Coupon.CouponRepository;

@Service
public class CouponServieImpl implements ICouponService {

	@Autowired
	private CouponRepository CouponRepo;

	@Override
	public Coupon getCouponById(int id) {
		// TODO Auto-generated method stub
		return CouponRepo.findById(id).get();
	}

	@Override
	public Coupon updateCouponByid(Coupon c) {
		// TODO Auto-generated method stub
		
		
			return CouponRepo.save(c);
	
	
	}

	@Override
	public void deleteCouponById(int id) {
		// TODO Auto-generated method stub
		Coupon c = CouponRepo.findById(id).get();
		if (c != null) {
			CouponRepo.delete(c);
		}

	}

	@Override
	public List<Coupon> getListCoupon() {
		// TODO Auto-generated method stub
		return CouponRepo.findAll();
	}

	@Override
	public Coupon createCoupon(Coupon c) {
		// TODO Auto-generated method stub

		return CouponRepo.save(c);
	}

	@Override
	public Coupon findCouponbyCouponCode(String c) {
		// TODO Auto-generated method stub
		
		
		return CouponRepo.findCouponByCouponCode(c);
	}

	@Override
	public Coupon findCouponByTypeCoupon(int id) {
		// TODO Auto-generated method stub
		return CouponRepo.findCouponByTypeCoupon(id)
				;
	}

}
