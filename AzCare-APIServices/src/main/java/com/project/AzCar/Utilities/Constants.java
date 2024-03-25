package com.project.AzCar.Utilities;

public class Constants {
	public static interface Roles {
        String USER = "ROLE_USER";
        String ADMIN = "ROLE_ADMIN";
        
    }
	public static interface ImgDir{
		String BRAND_DIR ="./UploadFiles/brandImage";
		String CAR_DIR="./UploadFiles/carImages";
		String USER_DIR ="./UploadFiles/userImages";
	}
	
	public static interface carStatus{
		String VERIFY="waiting_for_verify";
		String READY="ready_for_booking";
		String BUSY="on_other_booking";
		String BLOCK="blocked";
		String DECLINED ="declined";
	}
}
