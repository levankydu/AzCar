package com.project.AzCar.Services.Reviews;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
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

}
