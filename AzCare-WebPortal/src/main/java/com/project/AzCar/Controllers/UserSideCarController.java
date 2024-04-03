package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlateImageServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;
import com.project.AzCar.Utilities.OrderExtraFee;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserSideCarController {
	@Autowired
	private JavaMailSender mailSender;
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
	private PlusServiceServices plusServiceServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private FilesStorageServices fileStorageServices;
	@Autowired
	private UserServices userServices;
	@Autowired
	private OrderDetailsService orderServices;
	@Autowired
	private PaymentService paymentServices;
	@Autowired
	private PlateImageServices plateImageServices;

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
			@RequestParam("frontImg") MultipartFile frontImg, @RequestParam("behindImg") MultipartFile behindImg,
			@RequestParam("leftImg") MultipartFile leftImg, @RequestParam("rightImg") MultipartFile rightImg,
			@RequestParam("insideImg") MultipartFile insideImg, @ModelAttribute("carInfor") CarInfor carInfor,
			@ModelAttribute("extraFee") ExtraFee extraFee, @ModelAttribute("plusServices") PlusServices plusServices,
			@ModelAttribute("address") String address, BindingResult bindingResult, HttpServletRequest request)
			throws IOException {

		int min = 0; // Minimum value
		int max = 999999999; // Maximum value

		Random rand = new Random();
		int number = rand.nextInt(max - min + 1) + min;
		carInfor.setId(number);
		carInfor.setAddress(address);
		carInfor.setStatus(Constants.carStatus.VERIFY);

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users ownerId = userServices.findUserByEmail(email);
		carInfor.setCarOwnerId((int) ownerId.getId());
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

		try {
			System.out.println(carInfor);
			carServices.saveCarRegister(carInfor);
			var carDto = carServices.mapToDto(carInfor.getId());
			carDto.setCarmodel(brandServices.getModel(carInfor.getModelId()));
			sendEmail(email, carDto);
			return "successPage";

		} catch (Exception e) {
			System.out.println(e);
		}

		return "registerCar";

	}

	@PostMapping("/home/availablecars/details/{carId}")
	public String postRental(HttpServletRequest request, @ModelAttribute("order") OrderDetails orderdetails,
			@PathVariable("carId") String carId, @ModelAttribute("fromDate-string") String fromDate_string,
			@ModelAttribute("toDate-string") String toDate_string,
			@ModelAttribute("isSameProvince") String isSameProvince,
			@ModelAttribute("isSameDistrict") String isSameDistrict,
			@ModelAttribute("deliveryFee") String deliveryFee) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);
		orderdetails.setUserId((int) owner.getId());
		LocalTime currentTime = LocalTime.now();
		orderdetails.setFromDate(LocalDateTime.parse(fromDate_string + " " + currentTime,
				DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSS")));
		orderdetails.setToDate(LocalDateTime.parse(toDate_string + " " + currentTime,
				DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSS")));
		orderdetails.setSameProvince(isSameProvince.equals("1"));
		orderdetails.setSameDistrict(isSameDistrict.equals("1"));
		if (isSameProvince.equals("1") && isSameDistrict.equals("0")) {
			orderdetails.setExtraFee(new OrderExtraFee(Float.parseFloat(deliveryFee), 0, 0));
		}
		orderdetails.setHalfPaid(BigDecimal.valueOf(0));
		orderdetails.setStatus(Constants.orderStatus.WAITING);
		System.out.println(orderdetails);
		var orderDetails = orderServices.save(orderdetails);
//		var payment = new Payment();
//		payment.setUserId((int) owner.getId());
//		payment.setAmount(orderdetails.getTotalRent());
//		payment.setOrderDetailsId(orderDetails.getId());
//		payment.setDescription("Thue xe");
//		paymentServices.save(payment);
		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/carregister/success/")
	public String getMethodName() {
		return "successPage";
	}

	@GetMapping("/home/availablecars/details/{carId}")
	public String getDetailsPage(HttpServletRequest request, @PathVariable("carId") String carId, Model carDetails,
			Model address, Model fastBooking, Model carPlus, Model extraFee, Model fullAddress, Model provinceList,
			Model user) {
		var model = carServices.findById(Integer.parseInt(carId));
		var modelDto = carServices.mapToDto(model.getId());
		List<String> listProvince = provinceServices.getListCityString();

		var carExtraFee = extraFeeServices.findByCarId(model.getId());
		if (carExtraFee != null) {

			extraFee.addAttribute("extraFee", carExtraFee);
		}
		var carPLusService = plusServiceServices.findByCarId(model.getId());
		if (carPLusService != null) {

			carPlus.addAttribute("plusService", carPLusService);
		}
		modelDto.setCarmodel(brandServices.getModel(model.getModelId()));
		modelDto.setImages(carImageServices.getImgByCarId(model.getId()));
		for (var c : listProvince) {
			if (model.getAddress().contains(c)) {
				address.addAttribute("address", c);
			}
		}
		List<City> provinces = provinceServices.getListCity();
		provinceList.addAttribute("provinceList", provinces);

		fullAddress.addAttribute("fullAddress", model.getAddress());
		carDetails.addAttribute("carDetails", modelDto);
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);
		user.addAttribute("user", owner);
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

	@GetMapping("/home/availablecars")
	public String getAvailableCarsPage(Model ModelView, @RequestParam(name = "city", required = false) String city,
			HttpServletRequest request) {

		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> listDto = new ArrayList<>();
		List<String> brands = brandServices.getBrandList();
		List<String> categories = brandServices.getCategoryList();
		List<String> listProvince = provinceServices.getListCityString();
		List<City> provinces = provinceServices.getListCity();
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);
		for (var item : list) {
			if (item.getStatus().equals(Constants.carStatus.READY) && item.getCarOwnerId() != (int) owner.getId()) {
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
		}
		if (city != null) {
			var cityModel = provinceServices.findByCode(city);
			ModelView.addAttribute("province", cityModel.getFull_name());
			ModelView.addAttribute("listDistrict", districtServices.getDistricByProvinceCode(cityModel.getCode()));
			listDto.removeIf(t -> !t.getAddress().contains(cityModel.getName()));
			ModelView.addAttribute("carRegisterList", listDto);

		}

		ModelView.addAttribute("listBrand", brands);
		ModelView.addAttribute("listCategory", categories);
		ModelView.addAttribute("provinceList", provinces);
		ModelView.addAttribute("carRegisterList", listDto);
		return "availableCars";
	}

	@PostMapping("/home/availablecars")
	public String getResultPage(Model ModelView, HttpServletRequest request,
			@RequestParam(name = "isCarPlus", required = false, defaultValue = "false") boolean isCarPlus,
			@RequestParam(name = "isFastBooking", required = false, defaultValue = "false") boolean isFastBooking,
			@RequestParam(name = "isDiscount", required = false, defaultValue = "false") boolean isDiscount,
			@RequestParam(name = "carAddress") String carAddress, @RequestParam(name = "carBrand") String carBrand,
			@RequestParam(name = "carCate") String carCate, @RequestParam(name = "province") String province,
			@RequestParam(name = "districtSelect", defaultValue = "") String districtSelect,
			@RequestParam(name = "wardSelect", defaultValue = "") String wardSelect) {
		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> listDto = new ArrayList<>();
		List<String> brands = brandServices.getBrandList();
		List<String> categories = brandServices.getCategoryList();
		List<String> listProvince = provinceServices.getListCityString();
		List<City> provinces = provinceServices.getListCity();
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);

		for (var item : list) {

			var itemDto = carServices.mapToDto(item.getId());
			itemDto.setCarmodel(brandServices.getModel(item.getModelId()));
			itemDto.setImages(carImageServices.getImgByCarId(item.getId()));
			listDto.add(itemDto);
		}
		List<CarInforDto> filteredListDto = new ArrayList<>();
		
		if(carAddress.contains("Select")) {
			carAddress="";
		}
		for (var item : listDto) {
			if (carAddress.isEmpty() || item.getAddress().contains(carAddress)) {
				filteredListDto.add(item);
			}
		}
		if (!province.isEmpty() && !province.equals("--Select Province--")) {
			var city = provinceServices.findById(province);
			if (city != null) {
				ModelView.addAttribute("province", city.getFull_name());
				ModelView.addAttribute("listDistrict", districtServices.getDistricByProvinceCode(province));
				filteredListDto.removeIf(item -> !item.getAddress().contains(city.getFull_name()));

			} else {
				var city2 = provinceServices.findbyFullName(province);
				ModelView.addAttribute("province", city2.getFull_name());
				ModelView.addAttribute("listDistrict", districtServices.getDistricByProvinceCode(city2.getCode()));
				filteredListDto.removeIf(item -> !item.getAddress().contains(city2.getFull_name()));

			}

		}
		if (!districtSelect.isEmpty() && !districtSelect.equals("--Select District--")) {
			var district = districtServices.findbyId(districtSelect);
			if (district != null) {
				ModelView.addAttribute("district", district.getFull_name());
				ModelView.addAttribute("listWard", wardServices.getWardByDistrictCode(districtSelect));
				filteredListDto.removeIf(item -> !item.getAddress().contains(district.getFull_name()));
			} else {
				var district2 = districtServices.findbyFullName(districtSelect);
				ModelView.addAttribute("district", district2.getFull_name());
				ModelView.addAttribute("listWard", wardServices.getWardByDistrictCode(district2.getCode()));
				filteredListDto.removeIf(item -> !item.getAddress().contains(district2.getFull_name()));

			}

		}
		if (!wardSelect.isEmpty() && !wardSelect.equals("--Select Ward--")) {
			var ward = wardServices.findbyId(wardSelect);
			if (ward != null) {
				ModelView.addAttribute("ward", ward.getFull_name());
			} else {
				ModelView.addAttribute("ward", wardSelect);
			}

		}
		if (!carBrand.isEmpty() && !carBrand.equals("--Select Brand--")) {
			ModelView.addAttribute("carBrand", carBrand);
			filteredListDto.removeIf(item -> !item.getCarmodel().getBrand().contains(carBrand));
		}

		if (!carCate.isEmpty() && !carCate.equals("--Select Category--")) {
			ModelView.addAttribute("carCate", carCate);
			filteredListDto.removeIf(item -> !item.getCarmodel().getCategory().contains(carCate));
		}
		if (isCarPlus) {
			ModelView.addAttribute("carPlus", "true");
			filteredListDto.removeIf(item -> !item.isCarPlus());
		}

		if (isDiscount) {
			ModelView.addAttribute("discount", "true");
			filteredListDto.removeIf(item -> item.getDiscount() == 0);
		}

		for (var item : filteredListDto) {
			for (var c : listProvince) {
				if (item.getAddress().contains(c)) {
					item.setAddress(c);
				}
			}
		}
		filteredListDto.removeIf(t -> !t.getStatus().equals(Constants.carStatus.READY));
		filteredListDto.removeIf(t -> t.getOwner().getId() == owner.getId());
		ModelView.addAttribute("listBrand", brands);
		ModelView.addAttribute("listCategory", categories);
		ModelView.addAttribute("provinceList", provinces);
		ModelView.addAttribute("carRegisterList", filteredListDto);
		return "availableCars";
	}

	@GetMapping("/home/myplan/")
	public String getMyPlanPage(HttpServletRequest request, Model ModelView) {

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		List<CarInfor> list = carServices.getbyOwnerId((int) user.getId());
		List<CarInforDto> listDto = new ArrayList<>();
		List<PlateImages> listImg = plateImageServices.getAll();
		
		
		List<String> urlLicense = new ArrayList<>();
		for (var item : listImg) {
			if (item.getUserId() == user.getId()) {
				urlLicense.add(item.getImageUrl());
			}

		}
		for (var item : list) {
			var itemDto = carServices.mapToDto(item.getId());
			itemDto.setCarmodel(brandServices.getModel(item.getModelId()));
			itemDto.setImages(carImageServices.getImgByCarId(item.getId()));

			listDto.add(itemDto);
		}
		listImg.removeIf(t->t.getUserId()!=user.getId());
		listImg.removeIf(t->t.getStatus().equals(Constants.plateStatus.DECLINED));
		ModelView.addAttribute("ImgLicense", listImg);
		ModelView.addAttribute("listCar", listDto);
		ModelView.addAttribute("user",user);
		return "myPlans";
	}
	@PostMapping("/home/myplan/charge/")
	public String charge( HttpServletRequest request) {
		
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		user.setBalance(BigDecimal.valueOf(10000));
		userServices.saveUserReset(user);
		
		return"redirect:/home/myplan/";
	}
	@GetMapping("/home/availablecars/img/{filename}")
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

	@PostMapping("/home/myplan/")
	public String uploadDriveLicense(@RequestParam("frontImg") MultipartFile frontImg,
			@RequestParam("behindImg") MultipartFile behindImg, HttpServletRequest request) {

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users ownerId = userServices.findUserByEmail(email);

		String dir = "./UploadFiles/userImages" + "/" + ownerId.getId() + "-"
				+ ownerId.getEmail().replace(".", "-").replace("@", "-");
		Path path = Paths.get(dir);
		PlateImages frontImgModel = new PlateImages();
		PlateImages behindImgModel = new PlateImages();
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
		try {

			fileStorageServices.save(frontImg, dir);
			frontImgModel.setUserId(ownerId.getId());
			frontImgModel.setStatus(Constants.plateStatus.WAITING);
			frontImgModel.setImageUrl(frontImg.getOriginalFilename());
			plateImageServices.save(frontImgModel);

			fileStorageServices.save(behindImg, dir);
			behindImgModel.setUserId(ownerId.getId());
			behindImgModel.setStatus(Constants.plateStatus.WAITING);
			behindImgModel.setImageUrl(behindImg.getOriginalFilename());

			plateImageServices.save(behindImgModel);

		} catch (Exception e) {
			System.out.println(e);
		}

		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/license/{filename}")
	public ResponseEntity<Resource> getImageLicense(@PathVariable("filename") String filename,
			HttpServletRequest request) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users ownerId = userServices.findUserByEmail(email);

		String dir = "./UploadFiles/userImages" + "/" + ownerId.getId() + "-"
				+ ownerId.getEmail().replace(".", "-").replace("@", "-");

		Resource file = fileStorageServices.load(filename, dir);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/home/myplan/{filename}")
	public ResponseEntity<Resource> getImagePlan(@PathVariable("filename") String filename) throws IOException {
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

	private void sendEmail(String email, CarInforDto carDetails)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Successfull register your car";
		String content = "<p>Hello," + email + "</p>" + "<p>Thank you for registering your car rental with AzCar.</p>"
				+ "<p>Below are some main details of your car:</p>" + "<p><b>Car Details:</b></p>" + "<p>" + "Brand: "
				+ carDetails.getCarmodel().getBrand() + "</p>" + "<p>" + "Model: " + carDetails.getCarmodel().getModel()
				+ "</p>" + "<p>" + "Price: " + carDetails.getPrice() + " $/day" + "</p>" + "<p>" + "License Plates: "
				+ carDetails.getLicensePlates() + "</p>" + "<p>" + "Pick-up Location: " + carDetails.getAddress()
				+ "</p>" +

				"<p>This is to confirm that we already got info of your car, We will send you an email after verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

}
