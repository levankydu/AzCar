package com.project.AzCar.Repositories.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Users.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long>{

	@Query("SELECT u FROM Users u  WHERE u.email = :email")
	public Users findByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM Users u")
    List<Users> findAllUsers();
	
	
	
	
}
