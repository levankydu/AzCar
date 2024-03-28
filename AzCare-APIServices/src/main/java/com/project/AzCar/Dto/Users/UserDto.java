package com.project.AzCar.Dto.Users;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String id;

	private String name;

	private String email;

	private String password;

	private String confirmPassword;

	private String image;

	private String phone;

	private String firstName;
	private String lastName;
	private String gender;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dob;

	private boolean isEnabled;
	private String token;
	private boolean changePassword;

	@Transient
	public boolean isPasswordMatching() {
		if (password != null && password.equals(confirmPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
