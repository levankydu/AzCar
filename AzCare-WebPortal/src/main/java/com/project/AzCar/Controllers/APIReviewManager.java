package com.project.AzCar.Controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Comments.CommentsDTO;
import com.project.AzCar.Dto.KeywordIgnore.KeyWordIgnoreDTO;
import com.project.AzCar.Dto.Reviews.ReviewsDTO;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Services.IgnoreKeyword.IIgnoreKeywordService;
import com.project.AzCar.Services.Reviews.IReviewsService;
import com.project.AzCar.Services.Reviews.ReviewService;

@RestController
public class APIReviewManager {
	
	@Autowired
	private ReviewService reviewServices;
	@Autowired
	private IReviewsService reviewsService;
		@Autowired
	private IIgnoreKeywordService keywordService;
		@Autowired
		private ICommentsService cmtService;
		
	   @PostMapping("/reviews/update-status")

	    public ResponseEntity<?> updateStatus(@RequestBody ReviewsDTO  dto) {
		   System.out.println("Review id: " + dto);
		   
		
		   if(dto.getId() != null)
		   {
			   Reviews newReview = new Reviews();
			   newReview = reviewsService.updateStatus(dto.getId(),dto.getStatus());
			   ReviewsDTO temp = new ReviewsDTO();
			   temp.setId(newReview.getId());
			   temp.setStatus(newReview.getStatus());
			  
			   System.out.println(temp);
			
			   return ResponseEntity.ok("Status updated successfully" + temp);
		   }
		   return ResponseEntity.badRequest().body("why");

	   }
	   //create keyword Ignore
	   @PostMapping(value="/createkeyword")
	   public ResponseEntity<?> createKeyword(@RequestBody KeyWordIgnoreDTO IgnoreDTO)
	   {
		   IgnoreKeyword ignore  = new IgnoreKeyword();
		   ignore.setKeyword(IgnoreDTO.getKeyword());
		   keywordService.savekeyword(ignore);
		   return new ResponseEntity<String>("Create Successfully",HttpStatus.OK);
		   
	   }
	   
	   
	   //Update keyword Ignore
	   @PutMapping(value = "/editKeyword/{id}")
	   public ResponseEntity<?> updateKeyWord(@PathVariable("id") int id,@RequestBody KeyWordIgnoreDTO IgnoreDTO)
	   {
		   IgnoreKeyword ignore = keywordService.findByid(id);
		   
		   if(ignore !=null)
		   {
			   ignore.setKeyword(IgnoreDTO.getKeyword());
			   keywordService.savekeyword(ignore);
			   return ResponseEntity.ok().body(IgnoreDTO);
		   }
		   else {
	            return ResponseEntity.notFound().build();
	        }
	   }
	   // DELETE delete a keyword
	    @DeleteMapping("/deleteKeyword/{id}")
	    public ResponseEntity<?> deleteKeyword(@PathVariable("id") int id) {
	    	IgnoreKeyword keyword = keywordService.findByid(id);
	        if (keyword != null) {
	            keywordService.deleteByid(id);
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    //update Status Comments
	    @PutMapping(value="comment/update-comment/{id}")
	    public ResponseEntity<?> updateStatusComment(@PathVariable("id") int id,
	    		@RequestBody CommentsDTO dto)
	    {
	    	Comments cmt = cmtService.getCommentById(id);
	    	if(cmt !=null)
	    	{
	    		cmt.setStatus( ReviewStatus.valueOf(dto.getStatus()));
	    		cmtService.saveComment(cmt);
	    		return new ResponseEntity<String>("Đã Change",HttpStatus.OK);
	    		
	    	}
	    	else
	    	{
	    		return new ResponseEntity<String>("wrong",HttpStatus.BAD_REQUEST);
	    	}
	    	
	    
	    }
	    
	    @GetMapping("/reviews/car/{carId}")
	    public ResponseEntity<List<ReviewsDTO>> getReviewsByCarId(@PathVariable("carId") Long carId) {
	        List<Reviews> reviews = reviewServices.findAllReviewsByCarId(carId);
	        List<ReviewsDTO> reviewsDTO = reviews.stream().map(review -> {
	            ReviewsDTO dto = new ReviewsDTO();
	            dto.setId(review.getId());
	            dto.setCarId((int) review.getCarInfor().getId()); // Ép kiểu long sang int
	            dto.setComment(review.getComment());
	            dto.setUserName(review.getUser().getFirstName());
	            dto.setRating(review.getRating());
	            dto.setReviewDate(review.getReviewDate());
	            return dto;
	        }).collect(Collectors.toList());
	        return ResponseEntity.ok(reviewsDTO);
	    }
	    
	   
}
