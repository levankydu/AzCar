package com.project.AzCar.Services.Users;



import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;



@Service
public interface UserServices {

	void saveUser(UserDto userDto);
	void saveAdmin(UserDto userDto);

    Users findUserByEmail(String email);
}
