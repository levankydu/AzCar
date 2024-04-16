package com.project.AzCar.Services.HintText;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.HintText.HintText;
import com.project.AzCar.Repositories.HintText.HintTextRepositories;

@Service
public class HintTextServicesImpl implements HintTextServices {

	@Autowired
	private HintTextRepositories hintTextRepositories;

	@Override
	public List<HintText> findByType(String type) {
		return hintTextRepositories.findByType(type);
	}

}
