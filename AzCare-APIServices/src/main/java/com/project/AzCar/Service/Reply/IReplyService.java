package com.project.AzCar.Service.Reply;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import com.project.AzCar.Dto.Reply.ReplyDTO;
import com.project.AzCar.Entities.Reply.Reply;
@Service
public interface IReplyService {
	List<Reply> getListReplies();
	 public List<Reply> getAllReplyByCommentId(int id);
	 Reply saveReply(Reply rep);
	 
	
	
}
