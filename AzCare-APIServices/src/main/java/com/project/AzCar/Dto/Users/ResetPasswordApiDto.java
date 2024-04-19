package com.project.AzCar.Dto.Users;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordApiDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1201374535294322664L;
	private String password;
	public String token;
	
}
