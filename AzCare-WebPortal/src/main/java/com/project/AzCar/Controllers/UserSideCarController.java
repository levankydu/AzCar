package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.project.AzCar.Dto.DriverLicense.DriverLicenseBack;
import com.project.AzCar.Dto.DriverLicense.DriverLicenseFront;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Dto.Reply.ReplyDTO;
import com.project.AzCar.Dto.Reviews.ReviewsDTO;
import com.project.AzCar.Entities.CarThings.Contact;
import com.project.AzCar.Entities.CarThings.FavoriteCar;
import com.project.AzCar.Entities.Cars.CarImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.CarModelList;
import com.project.AzCar.Entities.Cars.ExtraFee;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Entities.Cars.PlusServices;
import com.project.AzCar.Entities.Comments.Comments;
import com.project.AzCar.Entities.Coupon.EnumCoupon;
import com.project.AzCar.Entities.Deposit.Cardbank;
import com.project.AzCar.Entities.HintText.HintText;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.Reply.Reply;
import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Entities.Users.Violation;
import com.project.AzCar.Repositories.CarThings.CarThingsRepo;
import com.project.AzCar.Repositories.CarThings.ContactRepo;
import com.project.AzCar.Repositories.Orders.ViolationRepository;
import com.project.AzCar.Repositories.ServiceAfterBooking.ServiceBookingRepositories;
import com.project.AzCar.Service.Comments.ICommentsService;
import com.project.AzCar.Service.Deposit.ICarbankService;
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
import com.project.AzCar.Services.Payments.ProfitCallBack;
import com.project.AzCar.Services.Reviews.IReviewsService;
import com.project.AzCar.Services.Reviews.ReviewService;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;
import com.project.AzCar.Utilities.OcrService;
import com.project.AzCar.Utilities.OrderExtraFee;
import com.project.AzCar.payments.paypal.PaypalService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import net.sourceforge.tess4j.TesseractException;

@Controller
public class UserSideCarController {
	@Autowired
	private OcrService ocrService;
	@Autowired
	private ICarbankService cardService;
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
	@Autowired
	private PaypalService paypalService;
	@Autowired
	private ContactRepo contactRepo;
	@Autowired
	private CarThingsRepo carThingsRepo;

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String PAYPAL_SUCCESS_URL = "checkout/pay/success";
	public static final String PAYPAL_CANCEL_URL = "checkout/pay/cancel";

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

	// Chá»§ xe check khi khÃ¡ch tráº£
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
					BigDecimal.valueOf(order.getExtraFee().getDeliveryFee()));
		}
		paymentServices.createNewRefund(car.getCarOwnerId(), order.getId(),
				BigDecimal.valueOf(order.getExtraFee().getSmellFee()));
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
			extraFee.setCleanningFee(extraFee.getCleanningFee() * 10 * 1000 * carInfor.getPrice().longValue() / 100);
			extraFee.setDecorationFee(extraFee.getDecorationFee() * 10 * 1000 * carInfor.getPrice().longValue() / 100);
			System.out.println(extraFee);
			extraFeeServices.save(extraFee);

		}
		if (isCarPlus) {
			carInfor.setCarPlus(true);
			plusServices.setCarRegisterId(number);
			plusServices.setFee(plusServices.getFee() * 10 * 1000 * carInfor.getPrice().longValue() / 100);
			System.out.println(plusServices);
			plusServiceServices.save(plusServices);
		}

		try {
			carInfor.setPrice(carInfor.getPrice().multiply(BigDecimal.valueOf(1000)));
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
			@ModelAttribute("isSameDistrict") String isSameDistrict, @ModelAttribute("deliveryFee") String deliveryFee)
			throws UnsupportedEncodingException, MessagingException {
		var carExtraFee = extraFeeServices.findByCarId(Integer.parseInt(carId));
		CarInfor carDetails = carServices.findById(Integer.parseInt(orderdetails.getCarId()));
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		orderdetails.setUserId((int) user.getId());
		orderdetails.setDiscount(carDetails.getDiscount());
		orderdetails.setOriginPrice(carDetails.getPrice());
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
		paymentServices.createNewLock(user.getId(), orderdetails.getId(),
				orderdetails.getTotalAndFeesWithoutInsurance());
		paymentServices.createNewProfit(user.getId(), BigDecimal.valueOf(orderdetails.getExtraFee().getInsurance()),
				new ProfitCallBack() {
					@Override
					public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
						user.setBalance(userBalance.subtract(amount));
					}
				}, false);
		CarInforDto carDetailsDto = carServices.mapToDto(carDetails.getId());
		BigDecimal discountAmount = orderdetails.getOriginPrice()
				.multiply(BigDecimal.valueOf(orderdetails.getDiscount()).divide(BigDecimal.valueOf(100)));
		BigDecimal priceAfterDiscount = orderdetails.getOriginPrice().subtract(discountAmount);
		BigDecimal subTotal = priceAfterDiscount.multiply(BigDecimal.valueOf(orderdetails.getDifferenceDate()))
				.add(BigDecimal.valueOf(orderdetails.getExtraFee().getDeliveryFee()));
		carDetailsDto.setCarmodel(brandServices.getModel(carDetails.getModelId()));
		String mailContent = "<p>Below are some main details of your car:</p>" + "<table>" + "<tr>" + "<th>Model: </th>"
				+ "<td>" + "[" + carDetailsDto.getCarmodel().getBrand() + "] " + carDetailsDto.getCarmodel().getModel()
				+ "</td>" + "</tr>" + "<tr>" + "<th>Year: </th>" + "<td>" + carDetailsDto.getCarmodel().getYear()
				+ "</td>" + "</tr>" + "<tr>" + "<th>Rental per day: </th>" + "<td>" + orderdetails.getOriginPrice()
				+ " VND" + "</td>" + "</tr>" + "<tr>" + "<th>Discount: </th>" + "<td>" + orderdetails.getDiscount()
				+ "% </td>" + "</tr>" + "<tr>" + "<th>Price After Discount: </th>" + "<td>" + priceAfterDiscount
				+ " VND" + "</td>" + "</tr>" + "<tr>" + "<th>From: </th>" + "<td>"
				+ orderdetails.getFromDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "</td>" + "</tr>"
				+ "<tr>" + "<th>To: </th>" + "<td>"
				+ orderdetails.getToDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "</td>" + "</tr>"
				+ "<tr>" + "<th>Total days: </th>" + "<td>" + orderdetails.getDifferenceDate() + "</td>" + "</tr>"
				+ "<tr>" + "<th>Delivery fee: </th>" + "<td>" + orderdetails.getExtraFee().getDeliveryFee() + " VND"
				+ "</td>" + "</tr>" + "<tr>" + "<th>Sub Total: </th>" + "<td> <h4>" + subTotal + " VND" + "</h4></td>"
				+ "</tr>" + "<tr>" + "<td colspan=2 class=text-center>Extra Fee</td>" + "</tr>" + "<tr>"
				+ "<td class=text-left>Cleanning Fee: </td>" + "<td class=text-right>"
				+ orderdetails.getExtraFee().getCleanFee() + " VND" + "</td>" + "</tr>" + "<tr>"
				+ "<td class=text-left>Smell Fee: </td>" + "<td class=text-right>"
				+ orderdetails.getExtraFee().getSmellFee() + " VND" + "</td>" + "</tr>" + "<tr>"
				+ "<td class=text-left>Insurance Fee: </td>" + "<td class=text-right>200,000 VND</td>" + "</tr>"
				+ "<tr>" + "<th>Total: </th>" + "<td>" + "<h4>"
				+ orderdetails.getTotalAndFees()
						.subtract(BigDecimal.valueOf(orderdetails.getExtraFee().getDeliveryFee()))
				+ " VND" + "</h4>" + "</td>" + "</tr>" + "</table>";
		orderServices.sendOrderEmail(email, "Place Order Successfully", "<p>Hello," + email + "</p>"
				+ "<p>Thank you for ordering with AzCar.</p>" + mailContent
				+ "<p>This is to confirm that we already got your order, We will send you an email after car owner accept or declined your order</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>");
		Users carOwner = userServices.findById(carDetails.getCarOwnerId());
		orderServices.sendOrderEmail(carOwner.getEmail(), "An order of" + "[" + carDetailsDto.getCarmodel().getBrand()
				+ "] " + carDetailsDto.getCarmodel().getModel() + "has been placed", mailContent);
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
		String email = request.getSession().getAttribute("emailLogin").toString();
		if (email.equals("admin@admin")) {
			return "redirect:/";
		}
		Users user = userServices.findUserByEmail(email);
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
		List<OrderDetails> orderDetailsOfThisCar = orderServices.getFromCarId(Integer.parseInt(carId));
		orderDetailsOfThisCar.removeIf(item -> !item.getStatus().equals(Constants.orderStatus.WAITING)
				&& !item.getStatus().equals(Constants.orderStatus.ACCEPTED));
		List<OrderDetails> userOrders = orderServices.getFromCreatedBy((int) user.getId());
		userOrders.removeIf(item -> !item.getStatus().equals(Constants.orderStatus.WAITING)
				&& !item.getStatus().equals(Constants.orderStatus.ACCEPTED));
		if (userOrders.size() > 0) {
			for (OrderDetails userOrder : userOrders) {
				boolean exists = orderDetailsOfThisCar.stream().anyMatch(item -> item.getId() == userOrder.getId());
				if (!exists) {
					orderDetailsOfThisCar.add(userOrder);
				}
			}
		}
		ModelView.addAttribute("orderDetailsOfThisCar", orderDetailsOfThisCar);

		List<City> provinces = provinceServices.getListCity();
		ModelView.addAttribute("provinceList", provinces);

		ModelView.addAttribute("fullAddress", model.getAddress());
		ModelView.addAttribute("carDetails", modelDto);
		Users customer = userServices.findUserByEmail(email);
		if (customer.isEnabled() == false) {
			ModelView.addAttribute("userViolated", true);
			ModelView.addAttribute("userBalanceeeeee", customer.getBalance());
		} else {
			ModelView.addAttribute("userViolated", false);
		}
		Users owner = userServices.findById(modelDto.getCarOwnerId());
		ModelView.addAttribute("customer", customer);
		ModelView.addAttribute("user", owner);
		System.out.println(owner.getPhone());
		List<PlateImages> plates = plateImageServices.getAll();
		plates.removeIf(item -> item.getUserId() != customer.getId());
		plates.removeIf(item -> !item.getStatus().equals(Constants.plateStatus.ACCEPTED));
		ModelView.addAttribute("isKhongHaveBangLai", plates.size() == 0);

		// Sally add
		// Láº¥y danh sÃ¡ch cÃ¡c review cho chiáº¿c xe vÃ  thÃªm vÃ o model
		List<Reviews> reviews = reviewServices.findAllReviewsByCarId(Integer.parseInt(carId));

		List<ReviewsDTO> listReviewsDTO = new ArrayList<>();
		if (!reviews.isEmpty()) {
			for (Reviews re : reviews) {
				if (re.getStatus().toString() != "Decline") {
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
		float tbtotal = 0;
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
		System.out.println(listReviewsDTO.size());
		if (listReviewsDTO.isEmpty()) {
			tbtotal = 0;
			ModelView.addAttribute("totalReviews", 0);
		} else {
			tbtotal = ((5 * fiveStar) + (4 * fourStar) + (3 * threeStar) + (2 * twoStar) + oneStar)
					/ (float) listReviewsDTO.size();
			ModelView.addAttribute("totalReviews", listReviewsDTO.size());
		}

		String formattedNumber = String.format("%.1f", tbtotal);
		System.out.println("Total TB: " + formattedNumber);

		ModelView.addAttribute("totalReviewsCount", formattedNumber);
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
		OrderDetails order = getOrderDetailsByCaridandUserid(model.getId(), customer.getId());
		// láº¥y status

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
			String firstName = re.getComment_id().getUser_id().getFirstName();
			String lastName = re.getComment_id().getUser_id().getLastName();

			if (lastName == null && firstName != null) {
				repDTO.setUser_name(firstName);
			} else {
				if (firstName == null && lastName != null) {
					repDTO.setUser_name(lastName);
				} else {
					if (firstName == null && lastName == null) {
						repDTO.setUser_name("NgÆ°á»�i tham gia áº©n danh");
					} else {
						repDTO.setUser_name(firstName + " " + lastName);
					}

				}
			}
//			
			lrepDTO.add(repDTO);

		}
		return lrepDTO;
	}

	@GetMapping("/home/availablecars/processingFavorite/{carId}")
	public ResponseEntity<String> processingFavorite(@PathVariable("carId") String carId, HttpServletRequest request) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		FavoriteCar favorite = carThingsRepo.getByCarAndUser(Integer.parseInt(carId), (int) user.getId());
		if (favorite == null) {
			FavoriteCar newFavor = new FavoriteCar();
			newFavor.setUserId((int) user.getId());
			newFavor.setCarId(Integer.parseInt(carId));
			carThingsRepo.save(newFavor);
			return new ResponseEntity<String>("Added", HttpStatus.OK);
		}
		carThingsRepo.delete(favorite);
		return new ResponseEntity<String>("Removed", HttpStatus.OK);
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
				FavoriteCar favor = carThingsRepo.getByCarAndUser(item.getId(), (int) owner.getId());
				itemDto.setFavorite(favor != null);
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
				if (tempC.getStatus().toString().contains("Pending")) {
					CommentsDTO tempDTO = new CommentsDTO();
					tempDTO.setId(tempC.getId());
					tempDTO.setContent(tempC.getContent());
					tempDTO.setUser_id(tempC.getUser_id().getId());
					String firstName = tempC.getUser_id().getFirstName();
					String lastName = tempC.getUser_id().getLastName();
					if (lastName == null && firstName != null) {
						tempDTO.setUser_name(firstName);
					} else {
						if (firstName == null && lastName != null) {
							tempDTO.setUser_name(lastName);
						} else {
							if (firstName == null && lastName == null) {
								tempDTO.setUser_name("NgÆ°á»�i tham gia áº©n danh");
							} else {
								tempDTO.setUser_name(firstName + " " + lastName);
							}

						}
					}

//					tempDTO.setUser_name(tempC.getUser_id().getFirstName() + " " + tempC.getUser_id().getLastName());
					tempDTO.setCar_id(car_id);
					List<ReplyDTO> reply = getAllReplyByComment_id(tempC.getId());
					tempDTO.setReply(reply);

					commentDTO.add(tempDTO);
				}

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
			System.out.println("Order Details: Ä‘Ã¢y " + order.getStatus());
			if (order.getStatus().contains("rentor_trip_done")) {
				if (order.isReview()) {
					System.out.println("náº¿u nÃ³ Ä‘Ã£ review thÃ¬ ko review ná»¯a" + order.isReview());
					return null;
				}
				System.out.println("dÃ²ng nÃ y ");
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
			@RequestParam(name = "wardSelect", defaultValue = "") String wardSelect,
			@RequestParam(name = "favorite", defaultValue = "false") boolean favoriteChoose) {
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
			FavoriteCar favor = carThingsRepo.getByCarAndUser(item.getId(), (int) owner.getId());
			itemDto.setFavorite(favor != null);
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
		if (favoriteChoose) {
			filteredListDto.removeIf(i -> {
				FavoriteCar favor = carThingsRepo.getByCarAndUser(i.getId(), (int) owner.getId());
				return favor == null;
			});
		}
		filteredListDto.removeIf(t -> !t.getStatus().equals(Constants.carStatus.READY));
		filteredListDto.removeIf(t -> t.getOwner().getId() == owner.getId());

		ModelView.addAttribute("favorite_check", favoriteChoose);
		ModelView.addAttribute("listBrand", brands);
		ModelView.addAttribute("listCategory", categories);
		ModelView.addAttribute("provinceList", provinces);
		ModelView.addAttribute("carRegisterList", filteredListDto);
		return "availableCars";
	}

	@GetMapping("/home/carregister/addNewModel/")
	public String getAddNewModelPage(Model ModelView) {
		List<String> brands = brandServices.getBrandList();

		List<String> categories = brandServices.getCategoryList();

		ModelView.addAttribute("categoryList", categories);
		ModelView.addAttribute("brandsList", brands);

		return "ifCarModelNotFound";

	}

	@PostMapping("/home/carregister/addNewModel")
	public String postAddNewModelPage(@ModelAttribute("carModel") CarModelList carModel, BindingResult bindingResult,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String email = request.getSession().getAttribute("emailLogin").toString();

		Random random = new Random();
		StringBuilder sb = new StringBuilder(10);
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		for (int i = 0; i < 10; i++) {

			int randomIndex = random.nextInt(characters.length());

			sb.append(characters.charAt(randomIndex));
		}
		String resultId = sb.toString();

		if (bindingResult.hasErrors()) {
//			return "";
			return "redirect:/home/carregister/addNewModel/" + "?error";
		} else {
			carModel.setObjectId(resultId);
			carModel.setStatus("waiting_for_accept_" + email);
			brandServices.saveBrand(carModel);
			sendEmailCreateNewCarModel(email, carModel);
		}
//		return "";
		return "redirect:/home/carregister/addNewModel/";

	}

	@GetMapping("/home/myplan/accepted/{orderId}")
	public String acceptRequestBooking(@PathVariable(name = "orderId") String orderId)
			throws UnsupportedEncodingException, MessagingException {
		var order = orderServices.getById(Integer.parseInt(orderId));
		order.setStatus(Constants.orderStatus.ACCEPTED);

		orderServices.save(order);
		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		paymentServices.createNewRefund(ownerId, order.getId(), order.getTotalRent().divide(BigDecimal.valueOf(2)));
		Users user = userServices.findById(order.getUserId());
		CarInforDto car = carServices.mapToDto(Integer.parseInt(order.getCarId()));
		String subject = "[" + car.getCarmodel().getBrand() + "] " + car.getCarmodel().getModel() + "Order Accepted";
		String mailcontent = "Your order is ACCEPTED by owner";
		orderServices.sendOrderEmail(user.getEmail(), subject, mailcontent);
		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/payFine")
	public String userPayFine(HttpServletRequest request) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);
		owner.setEnabled(true);
		userServices.saveUserReset(owner);
		List<Violation> violations = violationRepo.getByUserAndCarId(owner.getId(), 0, true,
				Constants.violations.USER_DECLINDED);
		for (Violation vio : violations) {
			vio.setEnabled(false);
			violationRepo.save(vio);
		}
		paymentServices.createNewProfit(owner.getId(), BigDecimal.valueOf(200000), new ProfitCallBack() {
			@Override
			public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
				user.setBalance(userBalance.subtract(amount));
			}
		}, false);

		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/ownerCarPayFine/{carId}")
	public String ownerCarPayFine(HttpServletRequest request, @PathVariable(name = "carId") String carId) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users owner = userServices.findUserByEmail(email);
		CarInfor car = carServices.findById(Integer.parseInt(carId));
		car.setStatus(Constants.carStatus.READY);
		List<Violation> violations = violationRepo.getByUserAndCarId(owner.getId(), Integer.parseInt(carId), true,
				Constants.violations.OWNER_DECLINED);
		for (Violation vio : violations) {
			vio.setEnabled(false);
			violationRepo.save(vio);
		}
		List<Violation> no_violations = violationRepo.getByUserAndCarId(owner.getId(), Integer.parseInt(carId), true,
				Constants.violations.NO_RESPONSE);
		for (Violation vio : no_violations) {
			vio.setEnabled(false);
			violationRepo.save(vio);
		}
		paymentServices.createNewProfit(owner.getId(), BigDecimal.valueOf(400000), new ProfitCallBack() {
			@Override
			public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
				user.setBalance(userBalance.subtract(amount));
			}
		}, false);

		return "redirect:/home/myplan/";
	}

	// car owner declines booking
	@GetMapping("/home/myplan/declined/{orderId}")
	public String declinedRequestBooking(@PathVariable(name = "orderId") String orderId)
			throws UnsupportedEncodingException, MessagingException {
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
		paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFeesWithoutInsurance());
		paymentServices.createNewExpense(order.getUserId(), BigDecimal.valueOf(order.getExtraFee().getInsurance()),
				new ProfitCallBack() {
					@Override
					public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
						user.setBalance(userBalance.add(amount));
					}
				}, false);
		Users user = userServices.findById(order.getUserId());
		CarInforDto carDTO = carServices.mapToDto(Integer.parseInt(order.getCarId()));
		String subject = "[" + carDTO.getCarmodel().getBrand() + "] " + carDTO.getCarmodel().getModel()
				+ "Order Declined";
		String reason = "";
		String mailcontent = "Your order is DECLINED by owner" + "<p>Reason: " + reason + "</p>";
		orderServices.sendOrderEmail(user.getEmail(), subject, mailcontent);

		Users owner = userServices.findById(ownerId);
		String mailcontentOnwer = "Decline successfully, you will get a violation ticket, if you have 3 tickets in a month your car will be disabled temporary!";
		orderServices.sendOrderEmail(owner.getEmail(), subject, mailcontentOnwer);

		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/rental_done/{orderId}")
	public String clientDoneRequestBooking(@PathVariable(name = "orderId") String orderId)
			throws UnsupportedEncodingException, MessagingException {
		var order = orderServices.getById(Integer.parseInt(orderId));
		order.setStatus(Constants.orderStatus.RENTOR_TRIP_DONE);
		orderServices.save(order);
		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		paymentServices.createNewRefund(ownerId, order.getId(), order.getTotalRent().multiply(BigDecimal.valueOf(0.4)));
		paymentServices.createNewProfit(ownerId, order.getTotalRent().multiply(BigDecimal.valueOf(0.1)),
				new ProfitCallBack() {
					@Override
					public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
					}
				}, false);
		Users user = userServices.findById(ownerId);
		CarInforDto carDTO = carServices.mapToDto(Integer.parseInt(order.getCarId()));
		String subject = "[" + carDTO.getCarmodel().getBrand() + "] " + carDTO.getCarmodel().getModel() + "Rentor Done";
		String mailcontent = "Your order is finished by customer";
		orderServices.sendOrderEmail(user.getEmail(), subject, mailcontent);
		return "redirect:/home/myplan/";
	}

	@GetMapping("/home/myplan/cancel_from_user/{orderId}")
	public String clientCancelBooking(@PathVariable(name = "orderId") String orderId)
			throws UnsupportedEncodingException, MessagingException {
		var order = orderServices.getById(Integer.parseInt(orderId));
		String status = order.getStatus();
		boolean isAccepted = false;
		if (status.equals("accepted")) {
			isAccepted = true;
			// return only 90% of totalRent and fees with insurance
			paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFeesWithoutInsurance());
			paymentServices.createNewProfit(order.getUserId(), order.getTotalRent().multiply(BigDecimal.valueOf(0.1)),
					new ProfitCallBack() {
						@Override
						public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
							user.setBalance(userBalance.subtract(amount));
						}
					}, false);
		}
		if (status.equals("waiting_for_verify")) {
			paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFeesWithoutInsurance());
			paymentServices.createNewExpense(order.getUserId(), BigDecimal.valueOf(order.getExtraFee().getInsurance()),
					new ProfitCallBack() {
						@Override
						public void onProcess(Users user, BigDecimal userBalance, BigDecimal amount) {
							user.setBalance(userBalance.add(amount));
						}
					}, false);
		}
		order.setStatus(Constants.orderStatus.RENTOR_DECLINED);
		orderServices.save(order);

		Violation vio = new Violation();
		vio.setUserId(order.getUserId());
		vio.setCarId(Integer.parseInt(order.getCarId()));
		vio.setReason(Constants.violations.USER_DECLINDED);
		violationRepo.save(vio);

		var ownerId = carServices.findById(Integer.parseInt(order.getCarId())).getCarOwnerId();
		Users owner = userServices.findById(ownerId);
		String reason = "";
		CarInforDto carDTO = carServices.mapToDto(Integer.parseInt(order.getCarId()));
		String subject = "[" + carDTO.getCarmodel().getBrand() + "] " + carDTO.getCarmodel().getModel()
				+ "Customer Declined";
		String mailcontent = "Your order is DECLINED by customer" + "<p>Reason: " + reason + "</p>";
		orderServices.sendOrderEmail(owner.getEmail(), subject, mailcontent);

		Users user = userServices.findById(order.getUserId());
		String mailcontentUser = "You have just canceled this order, you will get a violation ticket, if you have 3 tickets in a month you can not use our service temporary!";
		if (isAccepted) {
			mailcontentUser += "<p>Because this order was accepted, you will be charge 10% by total amount of this order as a pusnishment</p>";
		}
		orderServices.sendOrderEmail(user.getEmail(), subject, mailcontentUser);

		return "redirect:/home/myplan/";
	}

	@PostMapping("/home/myplan/emergencyContact")
	public ResponseEntity<String> emergencyContact(@RequestParam(name = "carId") String carId,
			@RequestParam(name = "description", required = true, defaultValue = "") String description,
			HttpServletRequest request) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		if (!description.isEmpty()) {
			Contact contact = new Contact();
			contact.setCarId(Integer.parseInt(carId));
			contact.setDescription(description);
			contact.setUserId((int) user.getId());
			contactRepo.save(contact);
			return new ResponseEntity<String>("Sent", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Failed", HttpStatus.OK);
	}

	@GetMapping("/home/myplan/")
	public String getMyPlanPage(HttpServletRequest request, Model ModelView) {
		String email = request.getSession().getAttribute("emailLogin").toString();
		Users user = userServices.findUserByEmail(email);
		if (user.isEnabled() == false) {
			ModelView.addAttribute("userViolated", true);
			ModelView.addAttribute("userBalanceeeeee", user.getBalance());
		} else {
			ModelView.addAttribute("userViolated", false);
		}

		orderServices.unrespondDetected(user);

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
			List<Violation> vioSize = violationRepo.getEnabledByCarId(itemDto.getId());
			vioSize.removeIf(i -> i.getUserId() != user.getId());
			itemDto.setActiveViolationAmount(vioSize.size());
			List<OrderDetailsDTO> finishedList = orderServices.getDTOFromCarId(item.getId());
			finishedList.removeIf(i -> !i.getStatus().equals("owner_trip_done"));
			itemDto.setFinishedOrders(finishedList.size());

			var historyBooking = "historyBooking" + itemDto.getCarmodel().getObjectId();
			List<OrderDetailsDTO> historyBookingList = orderServices.getDTOFromCarId(item.getId());
			ModelView.addAttribute(historyBooking, historyBookingList);
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

		List<Cardbank> cards = cardService.getListCardBankAdmin();
		cards.removeIf(i -> i.getActive() == EnumCoupon.InActive);
		Cardbank c = cards.get(0);

		if (request.getSession().getAttribute("add_driverLicense") != null) {
			ModelView.addAttribute("driverLicense", request.getSession().getAttribute("add_driverLicense"));
			request.getSession().removeAttribute("add_driverLicense");

		}
		ModelView.addAttribute("cardbank", c);
		Cardbank cardbank = cardService.findCardbankByUserId((int) user.getId());

		ModelView.addAttribute("cardbankuser", cardbank);

		ModelView.addAttribute("rentorDone", rentorDone);
		ModelView.addAttribute("orderList", latestOrders);
		ModelView.addAttribute("ImgLicense", listImg);
		ModelView.addAttribute("listCar", listDto);
		ModelView.addAttribute("user", user);

		return "myPlans";
	}

	@PostMapping("/home/myplan/updateCar")
	public String updateCar(@ModelAttribute("newDiscount") String newDiscount,
			@ModelAttribute("newPrice") String newPrice, @ModelAttribute("carId") String carId,
			@RequestParam(name = "isUpdateSerivces", required = false, defaultValue = "false") boolean isUpdateSerivces) {
		CarInfor car = carServices.findById(Integer.parseInt(carId));
		car.setDiscount(Integer.parseInt(newDiscount));
		BigDecimal priceData = new BigDecimal(newPrice).multiply(new BigDecimal(1000));

		if (isUpdateSerivces) {
			var plusService = plusServiceServices.findByCarId(car.getId());
			if (plusService != null) {
				BigDecimal plusOption = car.getPrice().divide(BigDecimal.valueOf(plusService.getFee()), 2,
						RoundingMode.HALF_UP);
				BigDecimal newFee = priceData.divide(plusOption, 2, RoundingMode.HALF_UP);
				plusService.setFee(newFee.longValue());
				plusServiceServices.save(plusService);
			}
			var extraFee = extraFeeServices.findByCarId(car.getId());
			if (extraFee != null) {
				BigDecimal cleanOption = car.getPrice().divide(BigDecimal.valueOf(extraFee.getCleanningFee()), 2,
						RoundingMode.HALF_UP);
				BigDecimal newClean = priceData.divide(cleanOption, 2, RoundingMode.HALF_UP);
				extraFee.setCleanningFee(newClean.longValue());
				BigDecimal decorOption = car.getPrice().divide(BigDecimal.valueOf(extraFee.getDecorationFee()), 2,
						RoundingMode.HALF_UP);
				BigDecimal newDecorFee = priceData.divide(decorOption, 2, RoundingMode.HALF_UP);
				extraFee.setDecorationFee(newDecorFee.longValue());
				extraFeeServices.save(extraFee);
			}
		}
		car.setPrice(priceData);
		System.out.println(carId);
		System.out.println(priceData);
		System.out.println(newDiscount);
		carServices.saveCarRegister(car);
		return "redirect:/home/myplan/";
	}

	@PostMapping("/home/myplan/paypal-charge/")
	public String paypalCharge(HttpServletRequest request) {
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
			@RequestParam("behindImg") MultipartFile behindImg, HttpServletRequest request, Model ModelView)
			throws IOException, TesseractException {

		String email = request.getSession().getAttribute("emailLogin").toString();
		Users ownerId = userServices.findUserByEmail(email);
		Integer countError = 0;
		DriverLicenseFront driverLicenseFront = new DriverLicenseFront();
		DriverLicenseBack driverLicenseBack = new DriverLicenseBack();
		var ocrResultFront = ocrService.ocr(frontImg).getResult();
		var ocrResultBack = ocrService.ocr(behindImg).getResult();
		// Regular expressions
		// Regular expressions
		Pattern licenseNumberPattern = Pattern.compile("No:\\s*(\\d+)");
		Pattern fullNamePattern = Pattern.compile("Full name:\\s*([^\\n]+)");
		Pattern dateOfBirthPattern = Pattern.compile("Date of Birth:\\s*(\\d{2}/\\d{2}/\\d{4})");
		Pattern licenseClassPattern = Pattern.compile("Class:\\s*([\\w\\d]+)");
		Pattern expiresPattern = Pattern.compile("Expires:\\s*(\\d{2}/\\d{2}/\\d{4})");
		Pattern isdriverLicense = Pattern.compile("DRIVER'S LICENSE");
		Pattern isdriverLicenseBack = Pattern.compile("CLASSIFICATION OF MOTOR VEHICLES");

		Matcher matcher;

		matcher = isdriverLicenseBack.matcher(ocrResultBack);
		if (matcher.find()) {
			driverLicenseBack.setDriverLicense(true);
		} else {
			driverLicenseBack.setDriverLicense(false);
			countError++;
		}
		matcher = licenseNumberPattern.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setLicenseNumber(matcher.group(1).trim());
		}

		matcher = fullNamePattern.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setFullName(matcher.group(1).trim());
		}

		matcher = dateOfBirthPattern.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setDateOfBirth(matcher.group(1).trim());
		}

		matcher = licenseClassPattern.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setLicenseClass(matcher.group(1).trim());
		}

		matcher = expiresPattern.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setExpires(matcher.group(1).trim());
		}

		matcher = isdriverLicense.matcher(ocrResultFront);
		if (matcher.find()) {
			driverLicenseFront.setDriverLicense(true);
		} else {
			countError++;
			driverLicenseFront.setDriverLicense(false);
		}

		String driverNo = driverLicenseFront.getLicenseNumber();
		Pattern licenseNumPattern = Pattern.compile("\\d{12}");
		if (driverNo == null) {
			countError++;
		} else {
			Matcher matcherNo = licenseNumPattern.matcher(driverNo);
			if (!matcherNo.find()) {
				countError++;
			}
		}

		if (driverLicenseFront.getFullName() == null) {
			countError++;
		}
		String dob = driverLicenseFront.getDateOfBirth();
		if (dob == null) {
			countError++;
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			try {
				LocalDate date = LocalDate.parse(dob, formatter);
				System.out.println("Parsed LocalDate: " + date);

			} catch (Exception e) {
				System.out.println("Invalid date format");
				countError++;
			}
		}

		if (driverLicenseFront.getLicenseClass() == null) {
			countError++;
		}
		String expires = driverLicenseFront.getExpires();
		if (expires == null) {
			countError++;
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			try {
				LocalDate expiresDay = LocalDate.parse(expires, formatter);
				System.out.println("Parsed LocalDate: " + expiresDay);

			} catch (Exception e) {
				System.out.println("Invalid date format");
				countError++;
			}
		}
		if (countError > 0) {

			request.getSession().setAttribute("add_driverLicense", "false");
			return "redirect:/home/myplan/";
		} else {
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
				frontImgModel.setLicenseNo(driverLicenseFront.getLicenseNumber());
				frontImgModel.setLicenseClass(driverLicenseFront.getLicenseClass());
				frontImgModel.setExpriedDay(driverLicenseFront.getExpires());
				frontImgModel.setRealName(driverLicenseFront.getFullName());
				plateImageServices.save(frontImgModel);

				fileStorageServices.save(behindImg, dir);
				behindImgModel.setUserId(ownerId.getId());
				behindImgModel.setStatus(Constants.plateStatus.WAITING);
				behindImgModel.setImageUrl(behindImg.getOriginalFilename());
				plateImageServices.save(behindImgModel);

			} catch (Exception e) {
				System.out.println(e);
			}
			request.getSession().setAttribute("add_driverLicense", "success");

			return "redirect:/home/myplan/";
		}

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
				+ "</p>" + "<p>" + "Price: " + carDetails.getPrice() + " VND/day" + "</p>" + "<p>" + "License Plates: "
				+ carDetails.getLicensePlates() + "</p>" + "<p>" + "Pick-up Location: " + carDetails.getAddress()
				+ "</p>" +

				"<p>This is to confirm that we already got info of your car, We will send you an email after verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	private void sendEmailCreateNewCarModel(String email, CarModelList carModel)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Successfull register your car";
		String content = "<p>Hello," + email + "</p>" + "<p>Thank you for registering your carModel with AzCar.</p>"
				+ "<p>Below are some main details of your new Models:</p>" + "<p><b>Car Details:</b></p>" + "<p>"
				+ "Brand: " + carModel.getBrand() + "</p>" + "<p>" + "Model: " + carModel.getModel() + "</p>" + "<p>"
				+ "Category: " + carModel.getCategory() + "</p>" + "<p>" + "Year: " + carModel.getYear() + "</p>"
				+ "<p>This is to confirm that we already got info of your car Model, We will send you an email after verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
}
