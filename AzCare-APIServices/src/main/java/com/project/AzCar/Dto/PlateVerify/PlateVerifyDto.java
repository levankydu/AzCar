package com.project.AzCar.Dto.PlateVerify;

import java.util.List;

import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Entities.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlateVerifyDto {

	private Users userModel;
	private List<PlateImages> plateImages;
	private String status;
	private String realName;
	private String licenseNo;
	private String expriedDay;
	private String licenseClass;

}
