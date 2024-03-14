package com.project.AzCar.Services.Users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;

@Service
public interface UserServices {
	void saveUser(UserDto userDto);
	boolean saveAdmin(UserDto userDto);
	UserDto editProfile(String email, UserDto updatedUserDto);
	void changePassword(String email, String newPassword);

	Users findUserByEmail(String email);

	List<Users> findAllUsers();

}
