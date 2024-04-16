package com.project.AzCar.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Users.UserServices;
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

	@Autowired
	private UserServices userServices;
	@Autowired
	private OrderDetailsService orderServices;
	@Autowired
	private ProvinceServices provinceServices;
	@Autowired
	private DistrictServices districtServices;
	@Autowired
	private WardServices wardServices;

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

	@GetMapping("/getCarsByUser")
	public List<CarInforDto> getCarByUserId(@RequestParam("emailLogin") String emailLogin) {
		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> result = new ArrayList<>();

		Users user = userServices.findUserByEmail(emailLogin);
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
		result.removeIf(t -> t.getCarOwnerId() != user.getId());
		return result;

	}

	@GetMapping("/getOrdersByCarId")
	public List<OrderDetailsDTO> getOrdersByCarId(@RequestParam("carId") String carId) {
		List<OrderDetailsDTO> mmmm = orderServices.getDTOFromCarId(Integer.parseInt(carId));
		for (var item : mmmm) {
			item.setUser(null);
		}
		mmmm.removeIf(i -> !i.getStatus().equals(Constants.orderStatus.DECLINED));
		return mmmm;

	}

	@GetMapping("/getProvinces")
	public List<City> getProvinces() {
		return provinceServices.getListCity();
	}

	@GetMapping("/getDistricts/{provinceCode}")
	public List<District> getDistricts(@RequestParam("provinceCode") String provinceCode) {
		return districtServices.getDistricByProvinceCode(provinceCode);
	}

	@GetMapping("/getDistricts/{districtCode}")
	public List<Ward> getWards(@RequestParam("districtCode") String districtCode) {
		return wardServices.getWardByDistrictCode(districtCode);
	}
}
