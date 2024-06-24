package com.project.AzCar.Dto.Users;

import lombok.Data;

@Data
public class ChangePasswordApiDto {
	private String email;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
}
