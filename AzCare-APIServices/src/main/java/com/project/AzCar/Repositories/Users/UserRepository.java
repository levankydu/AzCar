package com.project.AzCar.Repositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Users.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long>{

	
	public Users findByEmail(String email);
	
	
	
	
	
}
