package com.project.AzCar.Dto.Users;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String id;
	@NotEmpty(message = "Please enter your name.")
	private String name;

	@NotEmpty(message = "Please enter your email")
	@Email(message = "Email format must be correct")
	private String email;

	@NotEmpty(message = "Please enter password.")
	private String password;
	
	private String confirmPassword;

	private String image;
	
	private String phone;

	private String firstName;
	private String lastName;
	private String gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd ")
	private Date dob;
	private boolean isEnabled;
	
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
