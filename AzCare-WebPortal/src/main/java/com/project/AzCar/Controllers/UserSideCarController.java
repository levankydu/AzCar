package com.project.AzCar.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Entities.Cars.FastBooking;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.FastBookingServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;

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
	private FilesStorageServices fileStorageServices;
	@Autowired
	private UserServices userServices;

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
			@RequestParam("frontImg") MultipartFile frontImg, @RequestParam("behindImg") MultipartFile behindImg,
			@RequestParam("leftImg") MultipartFile leftImg, @RequestParam("rightImg") MultipartFile rightImg,
			@RequestParam("insideImg") MultipartFile insideImg, @ModelAttribute("carInfor") CarInfor carInfor,
			@ModelAttribute("extraFee") ExtraFee extraFee, @ModelAttribute("fastBooking") FastBooking fastBooking,
			@ModelAttribute("plusServices") PlusServices plusServices, @ModelAttribute("address") String address,
			BindingResult bindingResult, HttpServletRequest request) throws IOException {

		int min = 0; // Minimum value
		int max = 999999999; // Maximum value

		Random rand = new Random();
		int number = rand.nextInt(max - min + 1) + min;
		carInfor.setId(number);
		carInfor.setAddress(address);

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users ownerId = userServices.findUserByEmail(email);
		carInfor.setCarOwnerId(ownerId.getId().intValue());
		CarImages frontImgModel = new CarImages();
		CarImages behindImgModel = new CarImages();
		CarImages leftImgModel = new CarImages();
		CarImages rightImgModel = new CarImages();
		CarImages insideImgModel = new CarImages();

		String dir = "./UploadFiles/carImages" + "/" + carInfor.getModelId() + "-" + carInfor.getId();
		Path path = Paths.get(dir);

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

		try {

			fileStorageServices.save(frontImg, dir);
			frontImgModel.setName("frontImg");
			frontImgModel.setUrlImage(frontImg.getOriginalFilename());
			frontImgModel.setCarId(number);
			carImageServices.saveImg(frontImgModel);

			fileStorageServices.save(behindImg, dir);
			behindImgModel.setName("behindImg");
			behindImgModel.setUrlImage(behindImg.getOriginalFilename());
			behindImgModel.setCarId(number);
			carImageServices.saveImg(behindImgModel);

			fileStorageServices.save(leftImg, dir);
			leftImgModel.setName("leftImg");
			leftImgModel.setUrlImage(leftImg.getOriginalFilename());
			leftImgModel.setCarId(number);
			carImageServices.saveImg(leftImgModel);

			fileStorageServices.save(rightImg, dir);
			rightImgModel.setName("rightImg");
			rightImgModel.setUrlImage(rightImg.getOriginalFilename());
			rightImgModel.setCarId(number);
			carImageServices.saveImg(rightImgModel);

			fileStorageServices.save(insideImg, dir);
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

		} catch (Exception e) {
			System.out.println(e);
		}

		return "registerCar";

	}

	@GetMapping("/home/carregister/success/")
	public String getMethodName() {
		return "successPage";
	}

	@GetMapping("/home/availablecars/details/{carId}")
	public String getDetailsPage(@PathVariable("carId") String carId, Model carDetails,Model address,Model fastBooking,Model carPlus,Model extraFee) {
		var model = carServices.findById(Integer.parseInt(carId));
		var modelDto = carServices.mapToDto(model.getId());
		List<String> listProvince = provinceServices.getListCityString();
		var carFastBooking = bookingServices.findByCarId(model.getId());
		if(carFastBooking !=null) {
			fastBooking.addAttribute("fastBooking", carFastBooking);
		}
		var carExtraFee = extraFeeServices.findByCarId(model.getId());
		if(carExtraFee !=null) {
			
			extraFee.addAttribute("extraFee", carExtraFee);
		}
		var carPLusService = plusServiceServices.findByCarId(model.getId());
		if(carPLusService !=null) {
			
			carPlus.addAttribute("plusService", carPLusService);
		}
		modelDto.setCarmodel(brandServices.getModel(model.getModelId()));
		modelDto.setImages(carImageServices.getImgByCarId(model.getId()));
		for (var c : listProvince) {
			if (model.getAddress().contains(c)) {
				address.addAttribute("address",c);
			}
		}

		carDetails.addAttribute("carDetails", modelDto);
		return "carDetails";
	}

	@GetMapping("/home/availablecars/details/{carId}/{filename}")
	public ResponseEntity<Resource> getDetailsImage(@PathVariable("carId") String carId,
			@PathVariable("filename") String filename) {
		List<CarInfor> list = carServices.findAll();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/carImages/" + list.get(i).getModelId() + "-" + list.get(i).getId();
			Resource fileResource = fileStorageServices.load(filename, dir);
			if (fileResource == null) {
				i++;

			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileResource.getFilename() + "\"").body(fileResource);
			}

		}
		return null;
	}

	@GetMapping("/home/availablecars/")
	public String getAvailableCarsPage(Model carRegisterList) {
		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> listDto = new ArrayList<>();
		List<String> listProvince = provinceServices.getListCityString();
		for (var item : list) {
			var itemDto = carServices.mapToDto(item.getId());
			itemDto.setCarmodel(brandServices.getModel(item.getModelId()));
			itemDto.setImages(carImageServices.getImgByCarId(item.getId()));
			for (var c : listProvince) {
				if (item.getAddress().contains(c)) {
					itemDto.setAddress(c);
				}
			}

			listDto.add(itemDto);
		}
		carRegisterList.addAttribute("carRegisterList", listDto);
		return "availableCars";
	}

	@GetMapping("/home/availablecars/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) throws IOException {
		List<CarInfor> list = carServices.findAll();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/carImages/" + list.get(i).getModelId() + "-" + list.get(i).getId();
			Resource fileResource = fileStorageServices.load(filename, dir);
			if (fileResource == null) {
				i++;

			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileResource.getFilename() + "\"").body(fileResource);
			}

		}
		return null;

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
