package com.project.AzCar.Dto.Reply;

import lombok.Data;

@Data
public class ReplyDTO {
	String content;
	int comment_id;
	long carId;
	String user_name;

}
