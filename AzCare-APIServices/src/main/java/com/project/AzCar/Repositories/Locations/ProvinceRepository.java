package com.project.AzCar.Repositories.Locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Locations.City;

@Repository
public interface ProvinceRepository extends JpaRepository<City, String> {

	@Query(value = "SELECT c FROM City c")
	List<City> getListCity();

	@Query(value = "SELECT DISTINCT c.name FROM City c")
	List<String> getListString();

	@Query(value = "SELECT c FROM City c WHERE c.code=?1")
	City findbyId(String code);

	@Query(value = "SELECT c FROM City c WHERE c.code_name=?1")
	City findbyCode(String name);

	@Query(value = "SELECT c FROM City c WHERE c.full_name=?1")
	City findbyFullName(String fullName);
}
