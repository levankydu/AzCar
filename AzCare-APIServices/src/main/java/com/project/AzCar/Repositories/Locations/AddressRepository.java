package com.project.AzCar.Repositories.Locations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Locations.Addreess;

@Repository
public interface AddressRepository extends JpaRepository<Addreess, Integer> {

}
