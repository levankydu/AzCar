package com.project.AzCar.Repositories.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Users.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	@Query("SELECT u FROM Users u  WHERE u.email = ?1")
	public Users findByEmail(String email);

	@Query("SELECT u FROM Users u")
	List<Users> findAllUsers();

	public Users findByResetPasswordToken(String email);

	@Query(value = "SELECT distinct u FROM Users u WHERE u.resetPasswordToken = ?1")
	Users findUserByToken(String token);

	@Query(value = "SELECT u FROM Users u WHERE u.id=?1")
	Users findById(long id);
}
