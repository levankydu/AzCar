package com.project.AzCar.Dto.DriverLicense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicenseFront {

	private boolean isDriverLicense;
	private String licenseNumber;
	private String fullName;
	private String dateOfBirth;
	private String licenseClass;
	private String expires;

	@Override
	public String toString() {
		return "{" + "\"Driver License\":\"" + isDriverLicense + "\"," + "\"License Number\":\"" + licenseNumber + "\","
				+ "\"Full name\":\"" + fullName + "\"," + "\"Date of Birth\":\"" + dateOfBirth + "\"," + "\"Class\":\""
				+ licenseClass + "\"," + "\"Expires\":\"" + expires + "\"" + "}";
	}
}
