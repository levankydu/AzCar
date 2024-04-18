package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Dto.Comments.CommentsDTO;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Entities.Reviews.ReviewStatus;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.IgnoreKeyword.IIgnoreKeywordService;
import com.project.AzCar.Services.Users.UserServices;

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

	@PostMapping(value = "/comment/create")
	public ResponseEntity<?> createCommentByUserIdandCarid(@RequestParam(name = "userId", required = true) String email,
			@RequestParam(name = "carId", required = true) String carId,
			@RequestParam(name = "content", required = true) String comment) {

		var car = carServices.findById(Integer.parseInt(carId));
		var user = userServices.findUserByEmail(email);

		if (user == null || car == null) {
			return ResponseEntity.notFound().build();
		}

		Comments cmt = new Comments();
		List<IgnoreKeyword> lIgnore = ignoreService.listkeyword();
		Set<String> lkeyword = lIgnore.stream().map(IgnoreKeyword::getKeyword).collect(Collectors.toSet());

		// Split the comment into words and check for any ignore keywords
		Set<String> commentWords = Arrays.stream(comment.split("\\s+")).collect(Collectors.toSet());
		commentWords.retainAll(lkeyword); // This will keep only the words that are in the ignore list

		if (commentWords.isEmpty()) {
			System.out.println("cmt: " + comment);
			cmt.setContent(comment);
			cmt.setCar_id(car);
			cmt.setUser_id(user);
			cmt.setStatus(ReviewStatus.Pending);
			cmtService.saveComment(cmt);
			return ResponseEntity.ok().build();
		} else {
			return new ResponseEntity<List<String>>(new ArrayList<>(commentWords), HttpStatus.BAD_REQUEST);
		}
	}

	// GetListComment
	@GetMapping("/dashboard/comments")
	public String getDashboardComments(Model model) {
		List<Comments> cmt = cmtService.getAllComment();
		List<CommentsDTO> cmtDTO = new ArrayList<>();
		for (Comments temp : cmt) {
			CommentsDTO tempC = new CommentsDTO();
			tempC.setId(temp.getId());
			tempC.setCar_name(temp.getCar_id().getLicensePlates());
			tempC.setCar_id(temp.getId());
			tempC.setContent(temp.getContent());
			tempC.setUser_id(temp.getUser_id().getId());
			tempC.setUser_name(temp.getUser_id().getFirstName());
			tempC.setStatus(temp.getStatus().toString());
			cmtDTO.add(tempC);
		}

		model.addAttribute("listComments", cmtDTO);
		return "admin/CommentManager";

	}
}
