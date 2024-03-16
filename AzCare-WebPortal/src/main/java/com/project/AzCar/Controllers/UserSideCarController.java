package com.project.AzCar.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.AzCar.Entities.Cars.BrandImages;
import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Entities.Cars.FastBooking;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Locations.Addreess;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.FastBookingServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.AddressServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserSideCarController {

	@Autowired
	private BrandServices brandServices;

	@Autowired
	private ProvinceServices provinceServices;

	@Autowired
	private DistrictServices districtServices;

	@Autowired
	private WardServices wardServices;
	@Autowired
	private CarImageServices carImageServices;
	@Autowired
	private ExtraFeeServices extraFeeServices;
	@Autowired
	private FastBookingServices bookingServices;
	@Autowired
	private PlusServiceServices plusServiceServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private AddressServices addressServices;
	@Autowired
	private FilesStorageServices fileStorageServices;

	@GetMapping("/home/carregister/")
	public String getCarRegisterPage(Model brandList, Model provinceList) {

		List<String> brands = brandServices.getBrandList();
		List<City> provinces = provinceServices.getListCity();
		brandList.addAttribute("brandList", brands);
		provinceList.addAttribute("provinceList", provinces);
		return "registerCar";
	}

	@PostMapping("home/carregister")
	public String postCarRegister(
			@RequestParam(name = "isCarPlus", required = false, defaultValue = "false") boolean isCarPlus,
			@RequestParam(name = "isExtraFee", required = false, defaultValue = "false") boolean isExtraFee,
			@RequestParam(name = "isFastBooking", required = false, defaultValue = "false") boolean isFastBooking,
			@RequestParam("frontImg") MultipartFile frontImg,
			@RequestParam("behindImg") MultipartFile behindImg,
			@RequestParam("leftImg") MultipartFile leftImg, 
			@RequestParam("rightImg") MultipartFile rightImg,
			@RequestParam("insideImg") MultipartFile insideImg, 
			@ModelAttribute("carInfor") CarInfor carInfor,
			@ModelAttribute("extraFee") ExtraFee extraFee, 
			@ModelAttribute("fastBooking") FastBooking fastBooking,
			@ModelAttribute("plusServices") PlusServices plusServices, 
			@ModelAttribute("address") String address,
			BindingResult bindingResult,
			HttpServletRequest request) throws IOException {

		int min = 0; // Minimum value
		int max = 999999999; // Maximum value

		Random rand = new Random();
		int number =rand.nextInt(max - min + 1) + min;
		carInfor.setId(number);
		carInfor.setAddress(address);

		CarImages frontImgModel = new CarImages();
		CarImages behindImgModel = new CarImages();
		CarImages leftImgModel = new CarImages();
		CarImages rightImgModel = new CarImages();
		CarImages insideImgModel = new CarImages();

		try {
			
			fileStorageServices.save(frontImg, Constants.ImgDir.CAR_DIR);
			frontImgModel.setName("frontImg");
			frontImgModel.setUrlImage(frontImg.getOriginalFilename());
			frontImgModel.setCarId(number);
			carImageServices.saveImg(frontImgModel);

			fileStorageServices.save(behindImg, Constants.ImgDir.CAR_DIR);
			behindImgModel.setName("behindImg");
			behindImgModel.setUrlImage(behindImg.getOriginalFilename());
			behindImgModel.setCarId(number);
			carImageServices.saveImg(behindImgModel);

			fileStorageServices.save(leftImg, Constants.ImgDir.CAR_DIR);
			leftImgModel.setName("leftImg");
			leftImgModel.setUrlImage(leftImg.getOriginalFilename());
			leftImgModel.setCarId(number);
			carImageServices.saveImg(leftImgModel);

			fileStorageServices.save(rightImg, Constants.ImgDir.CAR_DIR);
			rightImgModel.setName("rightImg");
			rightImgModel.setUrlImage(rightImg.getOriginalFilename());
			rightImgModel.setCarId(number);
			carImageServices.saveImg(rightImgModel);

			fileStorageServices.save(insideImg, Constants.ImgDir.CAR_DIR);
			insideImgModel.setName("insideImg");
			insideImgModel.setUrlImage(insideImg.getOriginalFilename());
			insideImgModel.setCarId(number);
			carImageServices.saveImg(insideImgModel);

		} catch (Exception e) {
			System.out.println(e);
		}

		if (isExtraFee) {
			carInfor.setExtraFee(true);
			extraFee.setCarRegisterId(number);
			System.out.println(extraFee);
			extraFeeServices.save(extraFee);
			

		}
		if (isCarPlus) {
			carInfor.setCarPlus(true);
			plusServices.setCarRegisterId(number);
			System.out.println(plusServices);
			plusServiceServices.save(plusServices);
		}
		if (isFastBooking) {
			carInfor.setFastBooking(true);
			fastBooking.setCarRegisterdId(number);
			fastBooking.setFee(100);
			bookingServices.save(fastBooking);
			System.out.println(fastBooking);
		}
		
		try {
			System.out.println(carInfor);
			carServices.saveCarRegister(carInfor);
			return "successPage";
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return "registerCar";

	}
	@GetMapping("/home/carregister/success/")
	public String getMethodName(){
		return "successPage";
	}
	

	@GetMapping("/home/carregister/getCategory")
	public ResponseEntity<?> getCategory(@RequestParam("brandName") String brandName) {
		List<String> categoryList = brandServices.getCategoryListByBrand(brandName);

		return ResponseEntity.ok().body(Map.of("categoryList", categoryList));
	}

	@GetMapping("/home/carregister/getModel")
	public ResponseEntity<?> getModel(@RequestParam("brandName") String brandName,
			@RequestParam("cateName") String cateName) {
		List<String> modelList = brandServices.getModelListByCateAndBrand(brandName, cateName);

		return ResponseEntity.ok().body(Map.of("modelList", modelList));
	}

	@GetMapping("/home/carregister/getYear")
	public ResponseEntity<?> getYear(@RequestParam("brandName") String brandName,
			@RequestParam("cateName") String cateName, @RequestParam("modelName") String modelName) {
		List<String> yearList = brandServices.getYear(brandName, cateName, modelName);

		return ResponseEntity.ok().body(Map.of("yearList", yearList));
	}

	@GetMapping("/home/carregister/getModelId")
	public ResponseEntity<?> getModelId(@RequestParam("brandName") String brandName,
			@RequestParam("cateName") String cateName, @RequestParam("modelName") String modelName,
			@RequestParam("year") String year) {
		String modelId = brandServices.getModelId(brandName, cateName, modelName,
				Integer.parseInt(year == "" ? "0" : year));

		return ResponseEntity.ok().body(Map.of("modelId", modelId));
	}

	@GetMapping("/home/carregister/getDistrict")
	public ResponseEntity<?> getDistrict(@RequestParam("provinceCode") String provinceCode) {
		List<District> districts = districtServices.getDistricByProvinceCode(provinceCode);

		return ResponseEntity.ok().body(Map.of("districtList", districts));
	}

	@GetMapping("/home/carregister/getWard")
	public ResponseEntity<?> getWard(@RequestParam("districtCode") String districtCode) {
		List<Ward> wards = wardServices.getWardByDistrictCode(districtCode);

		return ResponseEntity.ok().body(Map.of("wardList", wards));
	}

}
