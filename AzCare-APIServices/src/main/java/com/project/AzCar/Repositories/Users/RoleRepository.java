package com.project.AzCar.Repositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.AzCar.Entities.Users.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long>{
    Roles findByName(String name);
}
