package com.project.AzCar.Repositories.Locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Locations.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, String>{

	@Query(value = "SELECT c FROM District c WHERE c.province_code =?1")
	List<District> getDistrictByProviceCode(String provinceCode);
	
	@Query(value = "SELECT DISTINCT c.name FROM District c")
	List<String> getListString();
	
	@Query(value = "SELECT c FROM District c WHERE c.code=?1")
	District findbyId(String id);

}
