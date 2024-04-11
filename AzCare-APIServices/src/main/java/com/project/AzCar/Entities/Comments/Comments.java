package com.project.AzCar.Entities.Comments;

import java.io.Serializable;


import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Reply.Reply;
import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Users.Users;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
@Table(name = "tb_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comments  implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String content;
	private ReviewStatus status;
	

    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarInfor car_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user_id;

}
