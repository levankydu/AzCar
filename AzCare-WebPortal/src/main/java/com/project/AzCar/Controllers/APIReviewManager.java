package com.project.AzCar.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.KeywordIgnore.KeyWordIgnoreDTO;
import com.project.AzCar.Dto.Reviews.ReviewsDTO;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Entities.Reviews.Reviews;
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
}
