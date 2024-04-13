package com.project.AzCar.Service.Reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Reply.Reply;
import com.project.AzCar.Repositories.Reply.ReplyRepository;

@Service
public class ReplyServiceImpl implements IReplyService {

	@Autowired
	private ReplyRepository replyRepo;

	@Override
	public List<Reply> getListReplies() {
		// TODO Auto-generated method stub
		return replyRepo.findAll();
	}

	public List<Reply> getAllReplyByCommentId(int id) {

		// TODO Auto-generated method stub
		List<Reply> replies = replyRepo.getAllReplyByCommentId(id);
		if (replies.isEmpty()) {
			return null;
		}

		return replies;
	}

	@Override
	public Reply saveReply(Reply rep) {
		// TODO Auto-generated method stub
		return replyRepo.save(rep);
	}

}
