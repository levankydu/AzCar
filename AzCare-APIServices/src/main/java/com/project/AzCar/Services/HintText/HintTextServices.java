package com.project.AzCar.Services.HintText;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.HintText.HintText;

@Service
public interface HintTextServices {

	List<HintText> findByType(String type);
}
