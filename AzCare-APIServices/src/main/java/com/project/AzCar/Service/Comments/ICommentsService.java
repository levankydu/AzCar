package com.project.AzCar.Service.Comments;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Comments.Comments;
@Service
public interface ICommentsService {
	 public List<Comments> getAllCommentsByCarId(int id);
	 public Comments getCommentById(int id);
	 public Comments saveComment(Comments comment);
}
	
