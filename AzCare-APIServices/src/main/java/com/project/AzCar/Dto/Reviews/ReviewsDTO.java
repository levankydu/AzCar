package com.project.AzCar.Dto.Reviews;

import java.util.Date;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Reviews.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewsDTO {
	private Long id;
	private String comment;
	private int rating;
	private Date reviewDate;
	private int carId;
	private String userName;
	private ReviewStatus status;
	
}
