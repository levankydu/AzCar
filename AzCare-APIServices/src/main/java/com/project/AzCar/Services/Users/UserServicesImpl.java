package com.project.AzCar.Services.Users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Users.Roles;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Users.RoleRepository;
import com.project.AzCar.Repositories.Users.UserRepository;
import com.project.AzCar.Utilities.Constants;

@Service
public class UserServicesImpl implements UserServices {

	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Override
	public void saveUser(UserDto userDto) {
		Roles role = roleRepo.findByName(Constants.Roles.USER);

        if (role == null)
            role = roleRepo.save(new Roles(Constants.Roles.USER));

        Users user = new Users(userDto.getName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(role));
        userRepo.save(user);
    }

		
	

	@Override
	public Users findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}




	@Override
	public void saveAdmin(UserDto userDto) {
		Roles role = roleRepo.findByName(Constants.Roles.ADMIN);

        if (role == null)
            role = roleRepo.save(new Roles(Constants.Roles.ADMIN));

        Users user = new Users("Admin","admin@admin", passwordEncoder.encode("123"),
                Arrays.asList(role));
        userRepo.save(user);
		
	}

}
