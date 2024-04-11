package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.AzCar.Dto.Coupons.CouponDTO;
import com.project.AzCar.Entities.Coupon.Coupon;
import com.project.AzCar.Services.Coupon.ICouponService;



@Controller
public class CouponController {
	
	@Autowired
	ICouponService couponService;
	
	@GetMapping(value ="/dashboard/coupon")
	public String getlistCoupon(Model model)
	{
		List<Coupon> coupon = couponService.getListCoupon();
		List<CouponDTO> couponDTO = new ArrayList<>();
		if(!coupon.isEmpty())
		{
			for(Coupon c : coupon)
			{
				CouponDTO tempC = new CouponDTO();
				tempC.setId(c.getId());
				tempC.setCouponCode(c.getCouponCode());
				tempC.setContent(c.getContent());
				tempC.setDiscountPercentage(c.getDiscountPercentage());
				tempC.setStatus(c.getStatus().toString());
				tempC.setQuantity(c.getQuantity());
				couponDTO.add(tempC);
			}
		model.addAttribute("ListCoupon", couponDTO);
		}
		
		return "admin/coupon";
	}
	

}
