package com.project.AzCar.Repositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Users.Roles;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>{
    Roles findByName(String name);
}
