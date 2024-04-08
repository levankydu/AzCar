package com.project.AzCar.Services.Reviews;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Repositories.Reviews.ReviewRepository;
@Service
public class ReviewsServiceImpl implements IReviewsService{
	@Autowired
	 ReviewRepository reviewRepository;
	@Override
	public List<Reviews> findRecentReviews() {
		// TODO Auto-generated method stub
		
		List <Reviews> list = reviewRepository.findRecentReviews();
		
		return list;
	}
	@Override
	 public Reviews  updateStatus(Long reviewId, ReviewStatus newStatus) {
		
	        Reviews review = reviewRepository.findById(reviewId)
	                .orElseThrow(() -> new RuntimeException("Review not found"));

	        review.setStatus(newStatus);
	      return   reviewRepository.save(review);
	    }
	

}
