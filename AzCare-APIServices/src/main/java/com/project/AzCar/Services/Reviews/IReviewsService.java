package com.project.AzCar.Services.Reviews;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Reviews.Reviews;


public interface IReviewsService {
	
	public List<Reviews> findRecentReviews();
	
	Reviews  updateStatus(Long reviewId, ReviewStatus newStatus);
	
}
