package com.project.AzCar.Dto.Users;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditApiDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3911648784357283688L;
	private long id;
	private String firstName;
	private String lastName;
	private String phone;
	private String gender;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dob;

}
