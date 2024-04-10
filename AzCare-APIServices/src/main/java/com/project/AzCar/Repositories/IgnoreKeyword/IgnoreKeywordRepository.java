package com.project.AzCar.Repositories.IgnoreKeyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
@Repository
public interface IgnoreKeywordRepository extends JpaRepository<IgnoreKeyword,Integer>{

}
