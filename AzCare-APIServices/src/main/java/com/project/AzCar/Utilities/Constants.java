package com.project.AzCar.Utilities;

public class Constants {
	public static interface Roles {
		String USER = "ROLE_USER";
		String ADMIN = "ROLE_ADMIN";

	}

	public static interface ImgDir {
		String BRAND_DIR = "./UploadFiles/brandImage";
		String CAR_DIR = "./UploadFiles/carImages";
		String USER_DIR = "./UploadFiles/userImages";
	}

	public static interface carStatus {
		String VERIFY = "waiting_for_verify";
		String READY = "ready_for_booking";
		String BUSY = "on_other_booking";
		String BLOCK = "blocked";
		String DECLINED = "declined";
		String VIOLATION_BLOCK = "violation_blocked";
	}

	public static interface orderStatus {
		String WAITING = "waiting_for_accept";
		String ACCEPTED = "accepted";
		String DECLINED = "declined";
		String RENTOR_DECLINED = "rentor_declined";
		String RENTOR_TRIP_DONE = "rentor_trip_done";
		String OWNER_TRIP_DONE = "owner_trip_done";
	}

	public static interface plateStatus {
		String WAITING = "waiting_for_verify";
		String ACCEPTED = "accepted";
		String DECLINED = "declined";

	}

	public static interface paymentStatus {
		String ADMIN_FEE = "admin_fee";
		String LOCKED = "locked";
		String REFUND = "unlocked";
		String TRANSFER = "transfer";
		String DEPOSIT = "deposit";
		String WITHDRAW = "withdraw";

	}

	public static interface violations {
		String NO_RESPONSE = "no_response_after_2_minutes";
		String OWNER_DECLINED = "onwer_declined";
		String USER_DECLINDED = "user_declined";
	}
}
