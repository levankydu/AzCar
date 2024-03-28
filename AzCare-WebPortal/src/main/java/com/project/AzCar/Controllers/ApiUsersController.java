package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Users.UserServices;

@RestController
public class ApiUsersController {

	@Autowired
	private UserServices userServices;

	@GetMapping("api/getUsers")
	public List<UserDto> getList() {
		List<UserDto> list = new ArrayList<>();
		List<Users> listUser = userServices.findAllUsers();
		for (var item : listUser) {
			var itemdDto = userServices.mapToDto((int) item.getId());
			list.add(itemdDto);
		}

		return list;
	}
}
