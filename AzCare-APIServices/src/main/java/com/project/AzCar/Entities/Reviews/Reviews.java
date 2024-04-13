package com.project.AzCar.Entities.Reviews;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbReviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reviews implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "car_id", nullable = false)
	private CarInfor carInfor;

	@ManyToOne()
	@JsonIgnore
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;

	@Enumerated(EnumType.STRING)
	private ReviewStatus status;

	private int rating; // Đánh giá từ 1 đến 5
	private String comment;
	private Date reviewDate;

	@Override
	public String toString() {
		return "Reviews [id=" + id + ", carInfor=" + carInfor + ", user=" + user + ", status=" + status + ", rating="
				+ rating + ", comment=" + comment + ", reviewDate=" + reviewDate + "]";
	}

	// Constructors, Getters, and Setters

}
