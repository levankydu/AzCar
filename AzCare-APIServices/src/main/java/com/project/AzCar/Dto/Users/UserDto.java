package com.project.AzCar.Dto.Users;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private long id;
	
	
	private String firstName;
	private String lastName;

	
	private String fullName;
	 
//	@NotEmpty(message = "Email not empty")

	private String email;

//	@NotEmpty(message = "Please enter password.")
	private String password;
//	@NotEmpty(message = "Please enter confirmpassword.")
	private String confirmPassword;

	private String image;

	private String phone;

	
	private String gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
