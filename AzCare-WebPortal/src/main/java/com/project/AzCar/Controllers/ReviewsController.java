package com.project.AzCar.Controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Reviews.ReviewService;
import com.project.AzCar.Services.Users.UserServices;

@Controller
public class ReviewsController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private ReviewService reviewServices;

	@GetMapping("/reviews/add")
	public String showReviewForm(Model model)
	{
		model.addAttribute(model);
		return "home/availablecars/details/692271992";
	}
	
    @PostMapping("/reviews/add")
	public String submitReview(@RequestParam(name="userId",required = false, defaultValue = "false") String email, 
			@RequestParam(name="carId" ,required = false, defaultValue = "false") String carId,
			@RequestParam(name="rating", required = false, defaultValue = "false")int rating, 
			@RequestParam(name="comment", required = false, defaultValue = "false") String comment) {

		

		var car = carServices.findById(Integer.parseInt(carId));
		Users user = userServices.findUserByEmail(email);
		Reviews review = new Reviews();
		review.setCarInfor(car);
		review.setUser(user);
		review.setRating(rating);
		review.setComment(comment);
		Date currentDate = new Date();
		
		review.setReviewDate(currentDate);
		

		reviewServices.save(review);
		System.out.println(review.getComment());
		return "redirect:/";
	}
  
}
