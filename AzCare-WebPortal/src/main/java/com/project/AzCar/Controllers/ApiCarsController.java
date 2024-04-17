package com.project.AzCar.Controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.OrderDetails;
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
	public List<CarInforDto> getAllCars() {

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

	@GetMapping("/getDistricts")
	public List<District> getDistricts(@RequestParam("provinceCode") String provinceCode) {
		return districtServices.getDistricByProvinceCode(provinceCode);
	}

	@GetMapping("/getWards")
	public List<Ward> getWards(@RequestParam("districtCode") String districtCode) {
		return wardServices.getWardByDistrictCode(districtCode);
	}

	@PostMapping("/postOrderDetails")
	public ResponseEntity<String> postOrderDetails(@RequestBody PhoneOrder order, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		System.out.println(order);
		boolean isPassed = true;
		String message = "Greate, your order is signed up successfully!";
		List<CarInforDto> cars = this.getAllCars();
		cars.removeIf(i -> i.getId() != Integer.parseInt(order.getCarId()));
		CarInforDto car = cars.get(0);

		List<OrderDetails> orderDetailsOfThisCar = orderServices.getFromCarId(Integer.parseInt(order.getCarId()));
		orderDetailsOfThisCar.removeIf(item -> !item.getStatus().equals(Constants.orderStatus.WAITING)
				&& !item.getStatus().equals(Constants.orderStatus.ACCEPTED));
		List<OrderDetails> userOrders = orderServices.getFromCreatedBy(Integer.parseInt(order.getUserId()));
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
				message = "These dates were picked (" + fromDateStr + " - " + toDateStr
						+ "), please choose another dates";
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
			BigDecimal totalRent = car.getPrice().multiply(BigDecimal.valueOf(daysDifference));
			orderdetails.setDifferenceDate((int) daysDifference);
			orderdetails.setTotalRent(totalRent);
			orderdetails.setDeliveryAddress(
					"12, PhoneD5, " + order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince());
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
					+ "<td class=text-left>Insurance Fee: </td>" + "<td class=text-right>$200</td>" + "</tr>" + "<tr>"
					+ "<th>Total: </th>" + "<td>" + "<h4>$"
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
}
