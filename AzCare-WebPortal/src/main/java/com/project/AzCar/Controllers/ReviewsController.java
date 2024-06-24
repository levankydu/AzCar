package com.project.AzCar.Controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Dto.Reviews.ReviewsDTO;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.IgnoreKeyword.IIgnoreKeywordService;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Reviews.IReviewsService;
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
	@Autowired
	private IReviewsService reviewsService;
	@Autowired
	private OrderDetailsService orderServices;
	@Autowired
	private IIgnoreKeywordService keywordService;
	@Autowired
	private IIgnoreKeywordService ignoreService;

	@GetMapping("/dashboard/reviews")
	public String showReviewForm(Model model) {
		List<Reviews> lReview = reviewsService.findAllReviews();

		model.addAttribute("listreviews", lReview);

		return "admin/reviewManager";
	}

	@GetMapping("/dashboard/ListIgnoreKey")
	public String showListIgnoreKey(Model model) {
		List<IgnoreKeyword> lkeyword = keywordService.listkeyword();
		System.out.println(lkeyword);
		model.addAttribute("listkeyword", lkeyword);
		return "admin/ListIgnoreKey";
	}

	@PostMapping("/reviews/add")
	public String submitReview(@RequestParam(name = "userId", required = false, defaultValue = "false") String email,
			@RequestParam(name = "carId", required = false, defaultValue = "false") String carId,
			@RequestParam(name = "rating", required = false, defaultValue = "false") int rating,
			@RequestParam(name = "comment", required = false, defaultValue = "false") String comment) {

		var car = carServices.findById(Integer.parseInt(carId));

		Users user = userServices.findUserByEmail(email);
		OrderDetails order = orderServices.getOrderDetailsByCarIdandUserId(Long.parseLong(carId), user.getId());
		if (order != null) {
			order.setReview(true);
		}

		Reviews review = new Reviews();
		review.setCarInfor(car);
		review.setUser(user);
		review.setRating(rating);

		review.setComment(comment);
		review.setStatus(ReviewStatus.valueOf("Pending"));
		Date currentDate = new Date();

		review.setReviewDate(currentDate);

		reviewServices.save(review);
		System.out.println(review.getComment());
		return "redirect:/home/availablecars/details/" + carId;

	}
	 
	


}
