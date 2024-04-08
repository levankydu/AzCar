package com.project.AzCar.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Users.UserServices;

@Controller
public class CommentController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private ICommentsService cmtService;
	
	
	@PostMapping(value ="/comment/create")
	public String  createCommentByUserIdandCarid(
			@RequestParam(name="userId",required = false, defaultValue = "false") String email, 
			@RequestParam(name="carId" ,required = false, defaultValue = "false") String carId,
			@RequestParam(name="content", required = false) String comment
			
			)
	{
		var car = carServices.findById(Integer.parseInt(carId));
		
		var user = userServices.findUserByEmail(email);
		if(user == null && car ==null)
		{
			return null;
		}
		Comments cmt = new Comments();
		cmt.setContent(comment);
		cmt.setCar_id( car);
		cmt.setUser_id(user);
		cmtService.saveComment(cmt);
		
		
		return "redirect:home/availablecars/details";
	}

}
