package com.project.AzCar.Repositories.Locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Locations.City;

@Repository
public interface ProvinceRepository extends JpaRepository<City, String>{

	@Query(value = "SELECT c FROM City c")
	List<City> getListCity();
	
	@Query(value = "SELECT DISTINCT c.name FROM City c")
	List<String> getListString();
}
