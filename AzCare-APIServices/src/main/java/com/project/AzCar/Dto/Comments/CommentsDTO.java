package com.project.AzCar.Dto.Comments;

import java.util.List;

import com.project.AzCar.Dto.Reply.ReplyDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentsDTO {
	int id;
	String content;
	long user_id;
	String user_name;
	long car_id;
	List<ReplyDTO> reply;
}
