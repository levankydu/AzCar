package com.project.AzCar.Repositories.HintText;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.HintText.HintText;
@Repository
public interface HintTextRepositories extends JpaRepository<HintText,Long> {
	
	@Query(value="SELECT c FROM HintText c WHERE c.type=?1 ")
	List<HintText> findByType (String type);

}