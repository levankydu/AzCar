package com.project.AzCar.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Coupons.CouponDTO;
import com.project.AzCar.Entities.Coupon.Coupon;
import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Coupon.ICouponService;
import com.project.AzCar.Services.Users.UserServices;

@RestController
public class APICoupon {
	
	@Autowired
	private ICouponService couponService;
	@Autowired
	private UserServices userService;
	
	
	//thay đổi coupon
	@PutMapping(value = "/updateCoupon/{id}")
	public ResponseEntity<?> updatedCoupon(@PathVariable("id") int id, @RequestBody CouponDTO coupon)
	{
		Coupon  c = couponService.getCouponById(id);
		if(c != null) {
			c.setCouponCode(coupon.getCouponCode());
			c.setContent(coupon.getContent());
			c.setExpirationDate(coupon.getExpirationDate());
			c.setStatus(EnumCoupon.InActive);
			c.setDiscountPercentage(coupon.getDiscountPercentage());
			c.setQuantity(coupon.getQuantity());
			couponService.updateCouponByid(c);
			
			return new ResponseEntity<String>("OKe", HttpStatus.OK );
		}
		
		
		
	return new ResponseEntity<String>("Lỗi ở đâu đó", HttpStatus.BAD_REQUEST );
	
	}
	//thêm coupon mới
	@PostMapping(value = "/createCoupon")
	public ResponseEntity<?> createCoupon(@RequestBody CouponDTO coupon)
	{
		Coupon  ctest = couponService.findCouponbyCouponCode(coupon.getCouponCode());
		Coupon c = new Coupon();
		if(ctest == null) {
			c.setCouponCode(coupon.getCouponCode());
			c.setContent(coupon.getContent());
			c.setExpirationDate(coupon.getExpirationDate());
			c.setStatus(EnumCoupon.InActive);
			c.setDiscountPercentage(coupon.getDiscountPercentage());
			c.setQuantity(coupon.getQuantity());
			couponService.createCoupon(c);
			
			return new ResponseEntity<String>("OKe", HttpStatus.OK );
		}
		
	return new ResponseEntity<String>("Lỗi ở đâu đó", HttpStatus.BAD_REQUEST );
	
	}
	//update trạng thái cho phép hoạt động
	@PutMapping(value = "/dashboard/updatedStatusCoupon/{id}")
	public ResponseEntity<?> updateStatusById(@PathVariable("id") int id, @RequestBody CouponDTO coupon)
	{
		Coupon  c = couponService.getCouponById(id);
		if(c != null) {
			if(c.getStatus().toString().contains("InActive"))
			{
				c.setStatus(EnumCoupon.Active);
				couponService.updateCouponByid(c);
				
				return new ResponseEntity<String>("OKe", HttpStatus.OK );
			}
			if(c.getStatus().toString().contains("Active"))
			{
				c.setStatus(EnumCoupon.InActive);
				couponService.updateCouponByid(c);	
				return new ResponseEntity<String>("OKe", HttpStatus.OK );
			}
		
		}
	return new ResponseEntity<String>("Lỗi ở đâu đó", HttpStatus.BAD_REQUEST );
	}


	public ResponseEntity<?> getCouponifuserused()
	{
		  List<Users>  luser = userService.findAllUsers();
		  
		  for(Users u : luser)
		  {
			  if(u.getScore() == 0)
			  {
				  
			  }
			  if(u.getScore() >300 && u.getScore() <1000)
			  {
				  
			  }
		  }
		  
		
		return null;
	}
}
