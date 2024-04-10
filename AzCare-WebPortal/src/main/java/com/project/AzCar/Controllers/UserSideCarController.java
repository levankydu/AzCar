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
import java.util.Comparator;
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
import com.project.AzCar.Dto.Comments.CommentsDTO;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Dto.Reply.ReplyDTO;
import com.project.AzCar.Dto.Reviews.ReviewsDTO;
import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.HintText.HintText;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.Reply.Reply;
import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Entities.Users.Violation;
import com.project.AzCar.Repositories.Orders.ViolationRepository;
import com.project.AzCar.Repositories.ServiceAfterBooking.ServiceBookingRepositories;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Service.Reply.IReplyService;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlateImageServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.HintText.HintTextServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Reviews.IReviewsService;
import com.project.AzCar.Services.Reviews.ReviewService;
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
	@Autowired
	private IReviewsService reviewsSv;
	@Autowired
	private ServiceBookingRepositories afterBookingRepositories;
	@Autowired
	private HintTextServices hintTextServices;
	@Autowired
	private ViolationRepository violationRepo;
	@Autowired
	private ReviewService reviewServices;
	@Autowired
	private ICommentsService commentsService;
	@Autowired
	private IReplyService repService;

	@GetMapping("/home/carregister/")

	public String getCarRegisterPage(Model ModelView) {

		List<String> brands = brandServices.getBrandList();
		List<City> provinces = provinceServices.getListCity();
		List<HintText> hintDescription = hintTextServices.findByType("description");
		List<HintText> hintRule = hintTextServices.findByType("rule");
		ModelView.addAttribute("rule", hintRule);
		ModelView.addAttribute("description", hintDescription);
		ModelView.addAttribute("brandList", brands);
		ModelView.addAttribute("provinceList", provinces);

		return "registerCar";
	}

	@PostMapping("home/myplan/rentalReview")
	public String retalReview(
			@RequestParam(name = "clean-check", required = false, defaultValue = "false") boolean cleanCheck,
			@RequestParam(name = "smell-check", required = false, defaultValue = "false") boolean smellCheck,
			@RequestParam(name = "decriptions", required = false, defaultValue = "false") String decriptions,
			@RequestParam(name = "carId", required = false, defaultValue = "false") String carId,
			@RequestParam(name = "orderId", required = false, defaultValue = "false") String orderId,
			@RequestParam(name = "imgUrl", required = false) MultipartFile imgUrl) {
		ServiceAfterBooking tuReview = new ServiceAfterBooking();
		var order = orderServices.getById(Integer.parseInt(orderId));
		var car = carServices.findById(Integer.parseInt(carId));
		if (cleanCheck) {
			paymentServices.createNewRefund(car.getCarOwnerId(), order.getId(),
					BigDecimal.valueOf(order.getExtraFee().getCleanFee()));
			tuReview.setCleanning(true);
		} else {
			paymentServices.createNewRefund(order.getUserId(), order.getId(),
					BigDecimal.valueOf(order.getExtraFee().getCleanFee()));
		}
		if (smellCheck) {
			paymentServices.createNewRefund(car.getCarOwnerId(), order.getId(),
					BigDecimal.valueOf(order.getExtraFee().getSmellFee()));
			tuReview.setSmelling(true);
		} else {
			paymentServices.createNewRefund(order.getUserId(), order.getId(),
					BigDecimal.valueOf(order.getExtraFee().getSmellFee()));
		}

		order.setStatus(Constants.orderStatus.OWNER_TRIP_DONE);
		orderServices.save(order);
		tuReview.setOrderId(order.getId());
		tuReview.setImgUrl(imgUrl.getOriginalFilename());
		tuReview.setDecriptions(decriptions);
		tuReview.setCarId(Integer.parseInt(carId));
		System.out.println(tuReview);

		tuReview = afterBookingRepositories.save(tuReview);

		String dir = "./UploadFiles/tuImages" + "/" + tuReview.getCarId() + "-" + tuReview.getId();
		Path path = Paths.get(dir);

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

		try {

			fileStorageServices.save(imgUrl, dir);

		} catch (Exception e) {
			System.out.println(e);
		}

		return "redirect:/home/myplan/";
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
		var carExtraFee = extraFeeServices.findByCarId(Integer.parseInt(carId));
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		orderdetails.setUserId((int) user.getId());
		LocalTime currentTime = LocalTime.now();

		try {
			orderdetails.setFromDate(LocalDateTime.parse(fromDate_string + " " + currentTime,
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSSSSS")));
			orderdetails.setToDate(LocalDateTime.parse(toDate_string + " " + currentTime,
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSSSSS")));
		} catch (Exception e) {
			orderdetails.setFromDate(LocalDateTime.parse(fromDate_string + " " + currentTime,
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSS")));
			orderdetails.setToDate(LocalDateTime.parse(toDate_string + " " + currentTime,
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSS")));
		}

		orderdetails.setSameProvince(isSameProvince.equals("1"));
		orderdetails.setSameDistrict(isSameDistrict.equals("1"));
		OrderExtraFee extra = new OrderExtraFee(0, carExtraFee != null ? carExtraFee.getCleanningFee() : 0,
				carExtraFee != null ? carExtraFee.getDecorationFee() : 0);
		if (isSameProvince.equals("1") && isSameDistrict.equals("0")) {
			extra.setDeliveryFee(Float.parseFloat(deliveryFee));
		}
		orderdetails.setExtraFee(extra);
		orderdetails.setStatus(Constants.orderStatus.WAITING);
		orderdetails.setReview(false);
		orderServices.save(orderdetails);
		paymentServices.createNewLock(user.getId(), orderdetails.getId(), orderdetails.getTotalAndFees());
		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/carregister/success/")
	public String getMethodName() {
		return "successPage";
	}

	@GetMapping("/home/availablecars/details/{carId}")
	public String getDetailsPage(HttpServletRequest request, @PathVariable("carId") String carId, Model ModelView) {
		var model = carServices.findById(Integer.parseInt(carId));
		var modelDto = carServices.mapToDto(model.getId());
		List<String> listProvince = provinceServices.getListCityString();

		var carExtraFee = extraFeeServices.findByCarId(model.getId());
		if (carExtraFee != null) {

			ModelView.addAttribute("extraFee", carExtraFee);
		}
		var carPLusService = plusServiceServices.findByCarId(model.getId());
		if (carPLusService != null) {

			ModelView.addAttribute("plusService", carPLusService);
		}
		modelDto.setCarmodel(brandServices.getModel(model.getModelId()));
		modelDto.setImages(carImageServices.getImgByCarId(model.getId()));
		for (var c : listProvince) {
			if (model.getAddress().contains(c)) {
				ModelView.addAttribute("address", c);
			}
		}
		List<City> provinces = provinceServices.getListCity();
		ModelView.addAttribute("provinceList", provinces);

		ModelView.addAttribute("fullAddress", model.getAddress());
		ModelView.addAttribute("carDetails", modelDto);
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users customer = userServices.findUserByEmail(email);
		Users owner = userServices.findById(modelDto.getCarOwnerId());
		ModelView.addAttribute("customer", customer);
		ModelView.addAttribute("user", owner);
		System.out.println(owner.getPhone());
		List<PlateImages> plates = plateImageServices.getAll();
		plates.removeIf(item -> item.getUserId() != customer.getId());
		plates.removeIf(item -> !item.getStatus().equals(Constants.plateStatus.ACCEPTED));
		ModelView.addAttribute("isKhongHaveBangLai", plates.size() == 0);

		// Sally add
		// Lấy danh sách các review cho chiếc xe và thêm vào model
		List<Reviews> reviews = reviewServices.findAllReviewsByCarId(Integer.parseInt(carId));

		List<ReviewsDTO> listReviewsDTO = new ArrayList<>();
		if(!reviews.isEmpty())
		{
			for(Reviews re : reviews)
			{
				if(re.getStatus().toString() !="Decline")
				{
					ReviewsDTO reDTO = new ReviewsDTO();
					reDTO.setId(re.getId());
					reDTO.setCarId(re.getCarInfor().getId());
					reDTO.setComment(re.getComment());
					reDTO.setRating(re.getRating());
					reDTO.setStatus(re.getStatus());
					reDTO.setReviewDate(re.getReviewDate());
					Users user1 = userServices.findById(re.getUser().getId());
					reDTO.setUserName(user1.getFirstName());
					listReviewsDTO.add(reDTO);
				}

			}

		}
		int fiveStar = 0, fourStar = 0, threeStar = 0, twoStar = 0, oneStar = 0;

		ModelView.addAttribute("reviews", listReviewsDTO);
		System.out.println("list Review" + listReviewsDTO);
		System.out.println("Order Details: " + model.getId() + " & ");
		if (!listReviewsDTO.isEmpty()) {
			System.out.println("reviews Details: " + listReviewsDTO + " & ");
			for (ReviewsDTO reviewsDTO : listReviewsDTO) {
				if (reviewsDTO.getRating() == 5) {
					fiveStar++;

				}
				if (reviewsDTO.getRating() == 4) {
					fourStar++;

				}
				if (reviewsDTO.getRating() == 3) {
					threeStar++;

				}
				if (reviewsDTO.getRating() == 2) {
					twoStar++;

				}
				if (reviewsDTO.getRating() == 1) {
					oneStar++;

				}
			}

		}
		ModelView.addAttribute("fiveStar", fiveStar);
		ModelView.addAttribute("fourStar", fourStar);
		ModelView.addAttribute("threeStar", threeStar);
		ModelView.addAttribute("twoStar", twoStar);
		ModelView.addAttribute("oneStar", oneStar);

		System.out.println("Order Details 123 : " + model.getId() + " & ");
		ModelView.addAttribute("reviews", listReviewsDTO);

		List<CommentsDTO> lcmtDTO;
		lcmtDTO = getCommentsByCarId(model.getId());

		System.out.println(model.getId());
		System.out.println(lcmtDTO);
		ModelView.addAttribute("comments", lcmtDTO);

		System.out.println("id Car Details: " + model.getId() + " ");
		OrderDetails order = getOrderDetailsByCaridandUserid(model.getId(), owner.getId());
		// lấy status

		System.out.println("Order Details: " + order + " & ");
		ModelView.addAttribute("Status_detail", order);

		return "carDetails";
	}

	public List<ReplyDTO> getAllReplyByComment_id(int id) {
		List<Reply> repl = repService.getAllReplyByCommentId(id);
		if (repl == null) {
			return null;
		}
		List<ReplyDTO> lrepDTO = new ArrayList<>();
		for (Reply re : repl) {
			ReplyDTO repDTO = new ReplyDTO();
			repDTO.setComment_id(re.getComment_id().getId());
			repDTO.setCarId(re.getComment_id().getCar_id().getId());
			repDTO.setContent(re.getContent());
			repDTO.setUser_name(re.getComment_id().getUser_id().getLastName());
			lrepDTO.add(repDTO);

		}
		return lrepDTO;
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

	// get Comments by Car Id
	public List<CommentsDTO> getCommentsByCarId(int car_id) {
		List<Comments> Lcomments = commentsService.getAllCommentsByCarId(car_id);
		List<CommentsDTO> commentDTO = new ArrayList<>();

		if (Lcomments != null) {
			for (Comments tempC : Lcomments) {
				CommentsDTO tempDTO = new CommentsDTO();
				tempDTO.setId(tempC.getId());
				tempDTO.setContent(tempC.getContent());
				tempDTO.setUser_id(tempC.getUser_id().getId());
				tempDTO.setUser_name(tempC.getUser_id().getFirstName());
				tempDTO.setCar_id(car_id);
				List<ReplyDTO> reply = getAllReplyByComment_id(tempC.getId());
				tempDTO.setReply(reply);

				commentDTO.add(tempDTO);
			}

			return commentDTO;
		}
		return null;

	}

	// getorderDetailsBycaridanduserid
	public OrderDetails getOrderDetailsByCaridandUserid(long carid, long userid) {
		System.out.println("Order Details: " + " & " + carid + " & " + userid);
		OrderDetails order = orderServices.getOrderDetailsByCarIdandUserId(carid, userid);
		if (order != null) {
			System.out.println("Order Details: đây " + order.getStatus());
			if (order.getStatus().contains("accepted")) {
				if (order.isReview()) {
					System.out.println("nếu nó đã review thì ko review nữa" + order.isReview());
					return null;
				}
				System.out.println("dòng này ");
				return order;
			}
			return null;

		}
		System.out.println("Order Details: " + order + " &" + carid + " &" + userid);
		return null;
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

		if (carAddress.contains("Select")) {
			carAddress = "";
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

	@GetMapping("/home/myplan/accepted/{orderId}")
	public String acceptRequestBooking(@PathVariable(name = "orderId") String orderId) {
		var order = orderServices.getById(Integer.parseInt(orderId));
		order.setStatus(Constants.orderStatus.ACCEPTED);
		orderServices.save(order);
		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		paymentServices.createNewRefund(ownerId, order.getId(), order.getTotalRent().divide(BigDecimal.valueOf(2)));

		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/declined/{orderId}")
	public String declinedRequestBooking(@PathVariable(name = "orderId") String orderId) {
		var order = orderServices.getById(Integer.parseInt(orderId));
		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		var car = carServices.findById(Integer.parseInt(order.getCarId()));
		order.setStatus(Constants.orderStatus.DECLINED);
		orderServices.save(order);
		Violation vio = new Violation();
		vio.setUserId(ownerId);
		vio.setCarId(car.getId());
		vio.setReason(Constants.violations.OWNER_DECLINED);
		violationRepo.save(vio);
		paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFees());
		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/rental_done/{orderId}")
	public String clientDoneRequestBooking(@PathVariable(name = "orderId") String orderId) {
		var order = orderServices.getById(Integer.parseInt(orderId));
		order.setStatus(Constants.orderStatus.RENTOR_TRIP_DONE);
		orderServices.save(order);
		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		paymentServices.createNewRefund(ownerId, order.getId(), order.getTotalRent().divide(BigDecimal.valueOf(2)));
		paymentServices.createNewLock(ownerId, order.getId(), order.getTotalRent().divide(BigDecimal.valueOf(10)));

		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/")
	public String getMyPlanPage(HttpServletRequest request, Model ModelView) {
		orderServices.unrespondDetected();

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		List<OrderDetailsDTO> orderList = orderServices.getDTOFromCreatedBy((int) user.getId());
		orderList.sort(Comparator.comparingLong(OrderDetailsDTO::getId).reversed());
		List<OrderDetailsDTO> latestOrders = new ArrayList<>(orderList.subList(0, Math.min(orderList.size(), 5)));
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
			List<OrderDetails> llll = orderServices.getFromCarId(item.getId());
			var name = "OrderListDto" + itemDto.getCarmodel().getObjectId();
			List<OrderDetailsDTO> mmmm = orderServices.getDTOFromCarId(item.getId());
			mmmm.removeIf(i -> !i.getStatus().equals(Constants.orderStatus.WAITING));
			ModelView.addAttribute(name, mmmm);
			llll.removeIf(i -> !i.getStatus().equals(Constants.orderStatus.WAITING));

			itemDto.setOrders(llll);
			listDto.add(itemDto);
		}
		listImg.removeIf(t -> t.getUserId() != user.getId());
		listImg.removeIf(t -> t.getStatus().equals(Constants.plateStatus.DECLINED));

		OrderDetailsDTO rentorDone = orderServices.getDTORentorTripDoneOrder();

		ModelView.addAttribute("rentorDone", rentorDone);
		ModelView.addAttribute("orderList", latestOrders);
		ModelView.addAttribute("ImgLicense", listImg);
		ModelView.addAttribute("listCar", listDto);
		ModelView.addAttribute("user", user);
		return "myPlans";
	}

	@PostMapping("/home/myplan/charge/")
	public String charge(HttpServletRequest request) {

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		user.setBalance(BigDecimal.valueOf(10000));
		userServices.saveUserReset(user);

		return "redirect:/home/myplan/";
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

	@GetMapping("/home/availablecars/flutter/img/{filename}")
	public ResponseEntity<Resource> getFlutterImage(@PathVariable("filename") String filename) throws IOException {
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
