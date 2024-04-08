package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Utilities.Constants;

@RestController
@RequestMapping("/api/cars")
public class ApiCarsController {

	@Autowired
	private CarServices carServices;

	@Autowired
	private ExtraFeeServices extraFeeServices;

	@Autowired
	private PlusServiceServices plusServiceServices;

	@Autowired
	private BrandServices brandServices;
	@Autowired
	private CarImageServices carImageServices;

	@GetMapping("/getAllCars")
	public List<CarInforDto> getMethodName() {

		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> result = new ArrayList<>();
		for (var item : list) {
			CarInforDto model = carServices.mapToDto(item.getId());

			var carExtraFee = extraFeeServices.findByCarId(model.getId());
			if (carExtraFee != null) {

				model.setExtraFeeModel(carExtraFee);
			}
			var carPLusService = plusServiceServices.findByCarId(model.getId());
			if (carPLusService != null) {

				model.setCarPlusModel(carPLusService);
			}
			model.setCarmodel(brandServices.getModel(item.getModelId()));
			model.setImages(carImageServices.getImgByCarId(item.getId()));
			result.add(model);
		}
		result.removeIf(t -> !t.getStatus().equals(Constants.carStatus.READY));
		return result;
	}

}