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

	void updateResetPasswordToken(String token, String email);

	Users findUserByEmail(String email);

	List<Users> findAllUsers();

	Users findUserByToken(String token);

	void saveUserReset(Users user);

	Users findById(long id);

	UserDto mapToDto(int id);

	boolean existsByEmail(String email);
	
	void updateUserStatus(String email, boolean isEnabled);
}
