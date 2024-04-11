package com.project.AzCar.Service.Comments;

import java.util.List;

import org.aspectj.bridge.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Repositories.Cars.CarRepository;
import com.project.AzCar.Repositories.Comments.CommentsRepository;
import com.project.AzCar.Repositories.Reply.ReplyRepository;


@Service
public class CommentsImpl implements ICommentsService{
	
	@Autowired
	private CommentsRepository commentsRepo;
	@Autowired
	private CarRepository carRepo;
	
	@Autowired
	private ReplyRepository replyRepo;
	@Override
	public List<Comments> getAllCommentsByCarId(int id) {
		CarInfor car = carRepo.findById(id).get();
		if(car!=null)
		{
			return commentsRepo.getAllCommentsByCarId(id);	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comments getCommentById(int id) {
		// TODO Auto-generated method stub
		return commentsRepo.findById(id).get();
	}

	@Override
	public Comments saveComment(Comments comment) {
		// TODO Auto-generated method stub
		
		return commentsRepo.save(comment);
	}

	@Override
	public List<Comments> getAllComment() {
		// TODO Auto-generated method stub
		return commentsRepo.findAll();
	}

	

}
