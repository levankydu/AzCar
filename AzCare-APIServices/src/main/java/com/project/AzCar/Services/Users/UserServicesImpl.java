package com.project.AzCar.Services.Users;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.Users.UserDto;
import com.project.AzCar.Entities.Cars.Payment;
import com.project.AzCar.Entities.Users.Roles;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Payments.PaymentRepository;
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
	private PaymentRepository paymentRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void saveUser(UserDto userDto) {
		Roles role = roleRepo.findByName(Constants.Roles.USER);

		if (role == null)
			role = roleRepo.save(new Roles(Constants.Roles.USER));

		Users user = new Users(userDto.getFullName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
				Arrays.asList(role));
		user.setEnabled(true);
		userRepo.save(user);
	}

	@Override
	public List<Users> findAllUsers() {
		return userRepo.findAllUsers();
	}

	@Override
	public Users findUserByEmail(String email) {

		return userRepo.findByEmail(email);
	}

	@Override
	public UserDto editProfile(String email, UserDto updatedUserDto) {
		Users existingUser = userRepo.findByEmail(email);

		if (existingUser != null) {
			existingUser.setFirstName(updatedUserDto.getFirstName());
			existingUser.setLastName(updatedUserDto.getLastName());
			existingUser.setPhone(updatedUserDto.getPhone());
			existingUser.setDob(updatedUserDto.getDob());
			existingUser.setGender(updatedUserDto.getGender());
			existingUser.setAddress(updatedUserDto.getAddress());
			userRepo.save(existingUser);
			return convertToDto(existingUser);
		} else {
			return null;
		}
	}

	private UserDto convertToDto(Users userEntity) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public boolean saveAdmin(UserDto userDto) {
		Roles role = roleRepo.findByName(Constants.Roles.ADMIN);

		if (role == null)
			role = roleRepo.save(new Roles(Constants.Roles.ADMIN));
		Users existingUser = userRepo.findByEmail("admin@admin");

		if (existingUser != null) {
			return false;

		} else {
			Users user = new Users("Admin", "admin@admin", passwordEncoder.encode("123"), Arrays.asList(role));
			BigDecimal userBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.valueOf(0);

			BigDecimal amount = BigDecimal.valueOf(100000000);
			var payment = new Payment();
			payment.setUserId((int) user.getId());
			payment.setToUserId(0);
			payment.setAmount(amount);
			payment.setDescription("Tien von cua Admin");
			payment.setStatus(Constants.paymentStatus.DEPOSIT);
			paymentRepository.save(payment);

			user.setBalance(userBalance.add(amount));
			userRepo.save(user);
			return true;
		}
	}

	@Override
	public void updateResetPasswordToken(String token, String email) {
		Users user = userRepo.findByEmail(email);

		if (user != null) {
			user.setResetPasswordToken(token);
			userRepo.save(user);
		}
	}

	public Users getResetPassword(String token) {
		return userRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(Users user, String newPassword) {
		String encodedNewPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedNewPassword);
		user.setResetPasswordToken(null);
		userRepo.save(user);
	}

	@Override
	public Users findUserByToken(String token) {
		return userRepo.findUserByToken(token);
	}

	@Override
	public void saveUserReset(Users user) {
		userRepo.save(user);
	}

	@Override
	public Users findById(long id) {
		return userRepo.findById(id);
	}

	@Override
	public UserDto mapToDto(int id) {
		var model = userRepo.findById(id);
		UserDto modelDto = this.modelMapper.map(model, UserDto.class);
		return modelDto;
	}

	@Override
	public boolean existsByEmail(String email) {
		var user = userRepo.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

}
