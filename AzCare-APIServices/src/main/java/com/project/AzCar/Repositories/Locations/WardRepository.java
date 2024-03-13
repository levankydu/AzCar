package com.project.AzCar.Repositories.Locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Locations.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, String>{
	
	@Query(value = "SELECT c FROM Ward c WHERE c.district_code =?1")
	List<Ward> getWardByDistrictCode(String districtCode);
}
