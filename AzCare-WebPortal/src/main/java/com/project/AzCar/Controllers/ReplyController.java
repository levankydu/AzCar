package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Reply.ReplyDTO;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Entities.Reply.Reply;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Service.Reply.IReplyService;
import com.project.AzCar.Services.IgnoreKeyword.IIgnoreKeywordService;

@RestController

public class ReplyController {
	
	// nếu click vào chổ reply thì sẽ hiển thị ra bản để reply sau đó gọi api này để post comments và refesh lại trang
	// chưa suy nghĩ ra nhưng hiện tại sẽ là như vậy
	@Autowired
	ICommentsService cmtService;
	@Autowired
	IReplyService replySer;
	@Autowired
	private IIgnoreKeywordService ignoreService;
	@PostMapping(value="/home/availablecars/details/submitReply")
	 public ResponseEntity<?> submitReply(@RequestBody ReplyDTO repDTO)
	 {
		 Reply rep = new Reply();
		 Comments cmt =cmtService.getCommentById(repDTO.getComment_id());
		 if(cmt!=null)
		 {	
			List<IgnoreKeyword> lIgnore = ignoreService.listkeyword();
			List<String> lkeyword = new ArrayList<>();
			for(IgnoreKeyword a: lIgnore)
			{
				lkeyword.add(a.getKeyword());
				
			}
			System.out.println("List Ignore: " + lkeyword);
			if(!ignoreService.isIgnore(repDTO.getContent(), lkeyword))
			{
			 rep.setComment_id(cmt);
			 rep.setContent(repDTO.getContent());
			 replySer.saveReply(rep);
			 return ResponseEntity.ok().build();
			}
		 }
		 else
		 {
			 return new ResponseEntity<String>("something will wrong",HttpStatus.BAD_REQUEST); 
		 }
		 
		 return new ResponseEntity<String>("something will wrong",HttpStatus.BAD_REQUEST); 
		 
		
	 }
}
