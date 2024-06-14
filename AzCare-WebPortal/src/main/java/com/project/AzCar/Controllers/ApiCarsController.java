package com.project.AzCar.Controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Dto.Orders.PhoneOrder;
import com.project.AzCar.Entities.CarThings.FavoriteCar;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Entities.Users.Violation;
import com.project.AzCar.Repositories.CarThings.CarThingsRepo;
import com.project.AzCar.Repositories.Cars.BrandRepository;
import com.project.AzCar.Repositories.Orders.ViolationRepository;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Payments.ProfitCallBack;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;
import com.project.AzCar.Utilities.OrderExtraFee;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

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
	private PaymentService paymentServices;
	@Autowired
	private BrandServices brandServices;
	@Autowired
	private CarImageServices carImageServices;
	@Autowired
	private BrandRepository brandRepo;
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
	@Autowired
	private ViolationRepository violationRepo;
	@Autowired
	private CarThingsRepo carThingsRepo;

	@GetMapping("/getAllCars")
	public List<CarInforDto> getAllCars() {

		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> result = new ArrayList<>();
		for (var item : list) {
			CarInforDto model = carServices.mapToDto(item.getId());
			/* Add violation count */
			List<Violation> vioSize = violationRepo.getEnabledByCarId(model.getId());
			model.setActiveViolationAmount(vioSize.size());
			/* add finished order count */
			List<OrderDetailsDTO> finishedList = orderServices.getDTOFromCarId(model.getId());
			finishedList.removeIf(i -> !i.getStatus().equals("owner_trip_done"));
			model.setFinishedOrders(finishedList.size());
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
			FavoriteCar favor = carThingsRepo.getByCarAndUser(item.getId(), (int) user.getId());
			model.setFavorite(favor != null);

			/* Add violation count */
			List<Violation> vioSize = violationRepo.getEnabledByCarId(model.getId());
			model.setActiveViolationAmount(vioSize.size());
			/* add finished order count */
			List<OrderDetailsDTO> finishedList = orderServices.getDTOFromCarId(model.getId());
			finishedList.removeIf(i -> !i.getStatus().equals("owner_trip_done"));
			model.setFinishedOrders(finishedList.size());

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

	@GetMapping("/getOrderByUser")
	public List<OrderDetailsDTO> getOrderByUser(@RequestParam("userId") String userId) {
		List<OrderDetailsDTO> list = orderServices.getDTOFromCreatedBy(Integer.parseInt(userId));
		Collections.sort(list, (a, b) -> b.getFromDate().compareTo(a.getFromDate()));
		return list;
	}

	@GetMapping("/getCarsExceptUserCar")
	public List<CarInforDto> getCarsExceptUserCar(@RequestParam("emailLogin") String emailLogin) {
		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> result = new ArrayList<>();

		Users user = userServices.findUserByEmail(emailLogin);
		for (var item : list) {
			CarInforDto model = carServices.mapToDto(item.getId());
			FavoriteCar favor = carThingsRepo.getByCarAndUser(item.getId(), (int) user.getId());
			model.setFavorite(favor != null);

			/* Add violation count */
			List<Violation> vioSize = violationRepo.getEnabledByCarId(model.getId());
			model.setActiveViolationAmount(vioSize.size());
			/* add finished order count */
			List<OrderDetailsDTO> finishedList = orderServices.getDTOFromCarId(model.getId());
			finishedList.removeIf(i -> !i.getStatus().equals("owner_trip_done"));
			model.setFinishedOrders(finishedList.size());

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
		result.removeIf(t -> t.getCarOwnerId() == user.getId());
		return result;
	}

	@GetMapping("/getOrdersByCarId")
	public List<OrderDetailsDTO> getOrdersByCarId(@RequestParam("carId") String carId) {
		List<OrderDetailsDTO> mmmm = orderServices.getDTOFromCarId(Integer.parseInt(carId));
		for (var item : mmmm) {
			item.setUser(null);
		}
		mmmm.removeIf(i -> i.getStatus().equals(Constants.orderStatus.DECLINED));
		mmmm.removeIf(i -> i.getStatus().equals(Constants.orderStatus.ACCEPTED));

		return mmmm;

	}

	@GetMapping("/getProvinces")
	public List<City> getProvinces() {
		return provinceServices.getListCity();
	}

	@GetMapping("/getDistricts")
	public List<District> getDistricts(@RequestParam("provinceCode") String provinceCode) {
		return districtServices.getDistricByProvinceCode(provinceCode);
	}

	@GetMapping("/getWards")
	public List<Ward> getWards(@RequestParam("districtCode") String districtCode) {
		return wardServices.getWardByDistrictCode(districtCode);
	}

	@GetMapping("/getBrands")
	public List<String> getBrands() {
		return brandRepo.getBrandList();
	}

	@GetMapping("/getCategoryListByBrand")
	public List<String> getCategoryListByBrand(@RequestParam("brandName") String brandName) {
		return brandRepo.getCategoryListByBrand(brandName);
	}

	@GetMapping("/getModelListByCateAndBrand")
	public List<String> getModelListByCateAndBrand(@RequestParam("brandName") String brandName,
			@RequestParam("cateName") String cateName) {
		return brandRepo.getModelListByCateAndBrand(brandName, cateName);
	}

	@GetMapping("/getYearList")
	public List<String> getYearList(@RequestParam("brandName") String brandName,
			@RequestParam("cateName") String cateName, @RequestParam("modelName") String modelName) {
		return brandRepo.getYearList(brandName, cateName, modelName);
	}

	@GetMapping("/getOrderOfThisCar")
	public List<OrderDetails> getOrderOfThisCar(@RequestParam("carId") String carId) {
		List<OrderDetails> orderDetailsOfThisCar = orderServices.getFromCarId(Integer.parseInt(carId));
		Collections.sort(orderDetailsOfThisCar, (a, b) -> b.getFromDate().compareTo(a.getFromDate()));
		return orderDetailsOfThisCar;
	}

	@GetMapping("/processFavorite")
	public ResponseEntity<String> processingFavorite(@RequestParam("carId") String carId,
			@RequestParam("userEmail") String email, HttpServletRequest request) {
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

	@GetMapping("/ownerAccepted")
	public ResponseEntity<String> acceptRequestBooking(@RequestParam("orderId") String orderId)
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
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/ownerDeclined")
	public ResponseEntity<String> declinedRequestBooking(@RequestParam("orderId") String orderId)
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

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/userRentalDone")
	public ResponseEntity<String> clientDoneRequestBooking(@RequestParam("orderId") String orderId)
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
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/userCancel")
	public ResponseEntity<String> clientCancelBooking(@RequestParam("orderId") String orderId)
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

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/ownerFinishReview")
	public ResponseEntity<String> retalReview(@RequestParam("cleanCheck") boolean cleanCheck,
			@RequestParam("smellCheck") boolean smellCheck, @RequestParam("description") String decriptions,
			@RequestParam(name = "carId", required = false, defaultValue = "false") String carId,
			@RequestParam(name = "orderId", required = false, defaultValue = "false") String orderId) {
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
		tuReview.setDecriptions(decriptions);
		tuReview.setCarId(Integer.parseInt(carId));

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@PostMapping("/postOrderDetails")
	public ResponseEntity<String> postOrderDetails(@RequestBody PhoneOrder order, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		System.out.println(order);
		boolean isPassed = true;
		String message = "Great, your order is signed up successfully!";
		List<CarInforDto> cars = this.getAllCars();
		cars.removeIf(i -> i.getId() != Integer.parseInt(order.getCarId()));
		CarInforDto car = cars.get(0);

		List<OrderDetails> orderDetailsOfThisCar = getNeededOrders(Integer.parseInt(order.getCarId()),
				Integer.parseInt(order.getUserId()));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime fromDatePhone = LocalDate.parse(order.getFromDate(), formatter).atStartOfDay();
		LocalDateTime toDatePhone = LocalDate.parse(order.getToDate(), formatter).atTime(23, 59, 59);

		for (OrderDetails orderItem : orderDetailsOfThisCar) {
			LocalDateTime fromDateItem = orderItem.getFromDate();
			LocalDateTime toDateItem = orderItem.getToDate();
			String fromDateStr = fromDateItem.toLocalDate().format(formatter);
			String toDateStr = toDateItem.toLocalDate().format(formatter);
			boolean isOverlapping = overlap(fromDateStr, toDateStr, fromDatePhone, toDatePhone);
			if (isOverlapping) {
				// Hai khoảng ngày trùng nhau
				isPassed = false;
				message = "These days were picked or you have picked another car at these days. (" + fromDateStr + " - "
						+ toDateStr + "), please choose another dates";
				break;
			}
		}

		float deliveryFee = 0;
		boolean isSameProvince = false;
		boolean isSameDistrict = false;
		if (car.getAddress().contains(order.getProvince())) {
			isSameProvince = true;
			if (!car.getAddress().contains(order.getDistrict())) {
				deliveryFee = car.isCarPlus() ? car.getCarPlusModel().getFee() : deliveryFee;
			} else {
				isSameDistrict = true;
			}
		} else {
			isPassed = false;
			message = "No delivery to this location";
		}

		if (isPassed) {
			OrderDetails orderdetails = new OrderDetails();
			Users user = userServices.findUserByEmail(order.getUserEmail());
			long daysDifference = ChronoUnit.DAYS.between(fromDatePhone, toDatePhone);
			BigDecimal priceAfterDiscount = car.getPrice().subtract(
					car.getPrice().multiply(BigDecimal.valueOf(car.getDiscount())).divide(BigDecimal.valueOf(100)));
			BigDecimal totalRent = priceAfterDiscount.multiply(BigDecimal.valueOf(daysDifference));
			orderdetails.setDifferenceDate((int) daysDifference);
			orderdetails.setTotalRent(totalRent);
			orderdetails.setDeliveryAddress(order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince());
			orderdetails.setUserId(Integer.parseInt(order.getUserId()));
			orderdetails.setCarId(order.getCarId());
			orderdetails.setDiscount(car.getDiscount());
			orderdetails.setOriginPrice(car.getPrice());
			orderdetails.setFromDate(fromDatePhone);
			orderdetails.setToDate(toDatePhone);
			orderdetails.setSameProvince(isSameProvince);
			orderdetails.setSameDistrict(isSameDistrict);
			OrderExtraFee extra = new OrderExtraFee(deliveryFee,
					car.getExtraFeeModel() != null ? car.getExtraFeeModel().getCleanningFee() : 0,
					car.getExtraFeeModel() != null ? car.getExtraFeeModel().getDecorationFee() : 0);
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
			BigDecimal subTotal = priceAfterDiscount.multiply(BigDecimal.valueOf(orderdetails.getDifferenceDate()))
					.add(BigDecimal.valueOf(orderdetails.getExtraFee().getDeliveryFee()));
			String mailContent = "<p>Below are some main details of your car:</p>" + "<table>" + "<tr>"
					+ "<th>Model: </th>" + "<td>" + "[" + car.getCarmodel().getBrand() + "] "
					+ car.getCarmodel().getModel() + "</td>" + "</tr>" + "<tr>" + "<th>Year: </th>" + "<td>"
					+ car.getCarmodel().getYear() + "</td>" + "</tr>" + "<tr>" + "<th>Rental per day: </th>" + "<td>$"
					+ orderdetails.getOriginPrice() + "</td>" + "</tr>" + "<tr>" + "<th>Discount: </th>" + "<td>"
					+ orderdetails.getDiscount() + "% </td>" + "</tr>" + "<tr>" + "<th>Price After Discount: </th>"
					+ "<td>$" + priceAfterDiscount + "</td>" + "</tr>" + "<tr>" + "<th>From: </th>" + "<td>"
					+ orderdetails.getFromDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "</td>" + "</tr>"
					+ "<tr>" + "<th>To: </th>" + "<td>"
					+ orderdetails.getToDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "</td>" + "</tr>"
					+ "<tr>" + "<th>Total days: </th>" + "<td>" + orderdetails.getDifferenceDate() + "</td>" + "</tr>"
					+ "<tr>" + "<th>Delivery fee: </th>" + "<td>$" + orderdetails.getExtraFee().getDeliveryFee()
					+ "</td>" + "</tr>" + "<tr>" + "<th>Sub Total: </th>" + "<td> <h4>$" + subTotal + "</h4></td>"
					+ "</tr>" + "<tr>" + "<td colspan=2 class=text-center>Extra Fee</td>" + "</tr>" + "<tr>"
					+ "<td class=text-left>Cleanning Fee: </td>" + "<td class=text-right>$"
					+ orderdetails.getExtraFee().getCleanFee() + "</td>" + "</tr>" + "<tr>"
					+ "<td class=text-left>Smell Fee: </td>" + "<td class=text-right>$"
					+ orderdetails.getExtraFee().getSmellFee() + "</td>" + "</tr>" + "<tr>"
					+ "<td class=text-left>Insurance Fee: </td>" + "<td class=text-right>200,000 VND</td>" + "</tr>"
					+ "<tr>" + "<th>Total: </th>" + "<td>" + "<h4>$"
					+ orderdetails.getTotalAndFees()
							.subtract(BigDecimal.valueOf(orderdetails.getExtraFee().getDeliveryFee()))
					+ "</h4>" + "</td>" + "</tr>" + "</table>";
			orderServices.sendOrderEmail(order.getUserEmail(), "Place Order Successfully", "<p>Hello,"
					+ order.getUserEmail() + "</p>" + "<p>Thank you for ordering with AzCar.</p>" + mailContent
					+ "<p>This is to confirm that we already got your order, We will send you an email after car owner accept or declined your order</p>"
					+ "<p>For any further assistance, feel free to contact us.</p>"
					+ "<p>Best regards,<br>AzCar Team</p>");
			Users carOwner = userServices.findById(car.getCarOwnerId());
			orderServices.sendOrderEmail(carOwner.getEmail(), "An order of" + "[" + car.getCarmodel().getBrand() + "] "
					+ car.getCarmodel().getModel() + "has been placed", mailContent);
		}

		return ResponseEntity.ok(message);
	}

	public static boolean overlap(String fromDateStr1, String toDateStr1, LocalDateTime fromDate2,
			LocalDateTime toDate2) {
		LocalDate fromDate1 = LocalDate.parse(fromDateStr1, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalDate toDate1 = LocalDate.parse(toDateStr1, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return !(toDate1.isBefore(fromDate2.toLocalDate()) || toDate2.toLocalDate().isBefore(fromDate1));
	}

	private List<OrderDetails> getNeededOrders(int carId, int userId) {
		List<OrderDetails> orderDetailsOfThisCar = orderServices.getFromCarId(carId);
		orderDetailsOfThisCar.removeIf(item -> !item.getStatus().equals(Constants.orderStatus.WAITING)
				&& !item.getStatus().equals(Constants.orderStatus.ACCEPTED));

		List<OrderDetails> userOrders = orderServices.getFromCreatedBy(userId);
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

		Collections.sort(orderDetailsOfThisCar, (a, b) -> a.getFromDate().compareTo(b.getFromDate()));
		return orderDetailsOfThisCar;
	}
}
