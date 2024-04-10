package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.IgnoreKeyword.IIgnoreKeywordService;
import com.project.AzCar.Services.Users.UserServices;

import ch.qos.logback.core.model.Model;

@Controller
public class CommentController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private ICommentsService cmtService;
	
	@Autowired
	private IIgnoreKeywordService ignoreService;
	
	
	
	@PostMapping(value ="/comment/create")
	public ResponseEntity<?>  createCommentByUserIdandCarid(
			@RequestParam(name="userId",required = false, defaultValue = "false") String email, 
			@RequestParam(name="carId" ,required = false, defaultValue = "false") String carId,
			@RequestParam(name="content", required = false) String comment
			
			)
	{
		var car = carServices.findById(Integer.parseInt(carId));
		
		var user = userServices.findUserByEmail(email);
		if(user == null && car ==null)
		{
			return ResponseEntity.notFound().build(); // Trả về mã lỗi 404 Not Found nếu user hoặc car không tồn tại}
		
		}Comments cmt = new Comments();
		List<IgnoreKeyword> lIgnore = ignoreService.listkeyword();
		List<String> lkeyword = new ArrayList<>();
		for(IgnoreKeyword a: lIgnore)
		{
			lkeyword.add(a.getKeyword());
			
		}
		System.out.println("List Ignore: " + lkeyword);
		if(!ignoreService.isIgnore(comment, lkeyword))
		{
			System.out.println("cmt: " + comment);
			cmt.setContent(comment);
			cmt.setCar_id( car);
			cmt.setUser_id(user);
			cmtService.saveComment(cmt);
			 return ResponseEntity.ok().build();
		}
		else
		{
			return new ResponseEntity<String>("Lỗi",HttpStatus.BAD_REQUEST);
		}
		
	 
	
		
		
		
		
	}
	
}


