package com.project.AzCar.Services.Orders;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Orders.OrderDetailsDTO;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.OrderDetails;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Entities.Users.Violation;
import com.project.AzCar.Repositories.Cars.CarRepository;
import com.project.AzCar.Repositories.Orders.OrderRepository;
import com.project.AzCar.Repositories.Orders.ViolationRepository;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CarServices carServices;
	@Autowired
	private CarRepository carRepo;
	@Autowired
	private CarImageServices carImageServices;
	@Autowired
	private BrandServices brandServices;
	@Autowired
	private ViolationRepository violationRepo;
	@Autowired
	private PaymentService paymentServices;

	@Autowired
	private UserServices userServices;

	@Override
	public OrderDetails getById(int id) {
		return orderRepository.getById(id);
	}

	@Override
	public List<OrderDetails> getFromCreatedBy(int id) {
		return orderRepository.getFromCreatedBy(id);
	}

	@Override
	public OrderDetails save(OrderDetails order) {
		var saveModel = orderRepository.save(order);
		return saveModel;
	}

	@Override
	public OrderDetails update(OrderDetails order) {
		var updatedModel = orderRepository.save(order);
		return updatedModel;
	}

	@Override
	public List<OrderDetailsDTO> getDTOFromCreatedBy(int id) {
		List<OrderDetails> originOrders = orderRepository.getFromCreatedBy(id);
		List<OrderDetailsDTO> dtos = new ArrayList<>();
		for (OrderDetails order : originOrders) {
			OrderDetailsDTO dto = this.modelMapper.map(order, OrderDetailsDTO.class);
			CarInfor carRaw = carServices.findById(Integer.parseInt(order.getCarId()));
			CarInforDto car = carServices.mapToDto(Integer.parseInt(order.getCarId()));
			car.setCarmodel(brandServices.getModel(carRaw.getModelId()));
			car.setImages(carImageServices.getImgByCarId(car.getId()));
			dto.setCar(car);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<OrderDetailsDTO> getDTOFromCarId(int id) {
		List<OrderDetails> originOrders = orderRepository.getFromCarId(id);
		List<OrderDetailsDTO> dtos = new ArrayList<>();
		for (OrderDetails order : originOrders) {
			OrderDetailsDTO dto = this.modelMapper.map(order, OrderDetailsDTO.class);
			CarInfor carRaw = carServices.findById(Integer.parseInt(order.getCarId()));
			CarInforDto car = carServices.mapToDto(Integer.parseInt(order.getCarId()));
			Users user = userServices.findById(order.getUserId());
			car.setCarmodel(brandServices.getModel(carRaw.getModelId()));
			car.setImages(carImageServices.getImgByCarId(car.getId()));
			dto.setUser(user);
			dto.setCar(car);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public void unrespondDetected(Users currentUser) {
		List<OrderDetails> orderList = orderRepository.getAll();
		for (OrderDetails order : orderList) {
			CarInfor car = carServices.findById(Integer.parseInt(order.getCarId()));
			long ownerId = car.getCarOwnerId();
			if(order.getStatus().equals(Constants.orderStatus.WAITING)) {
				long seconds = Duration.between(order.getCreatedAt(), LocalDateTime.now()).getSeconds();
				if (seconds >= 9999999) {
					Violation vio = new Violation();
					vio.setUserId(ownerId);
					vio.setCarId(car.getId());
					vio.setReason(Constants.violations.NO_RESPONSE);
					violationRepo.save(vio);
					order.setStatus(Constants.orderStatus.DECLINED);
					orderRepository.save(order);

					paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFees());
				}
			}
			List<Violation> noRes_violations = violationRepo.getByUserAndCarId(ownerId, car.getId(), true, Constants.violations.NO_RESPONSE);
			List<Violation> declined_violations = violationRepo.getByUserAndCarId(ownerId, car.getId(), true, Constants.violations.OWNER_DECLINED);
			if (noRes_violations.size() + declined_violations.size() >= 3) {
				car.setStatus(Constants.carStatus.DECLINED);
				carRepo.save(car);
			}
		}
		List<Violation> userViolations = violationRepo.getByUserAndCarId(currentUser.getId(), 0, true, Constants.violations.USER_DECLINDED);
		if (userViolations.size() >= 3) {
			currentUser.setEnabled(false);
			userServices.saveUserReset(currentUser);
		}
	}

	@Override
	public List<OrderDetails> getFromCarId(int carId) {
		return orderRepository.getFromCarId(carId);
	}

	@Override
	public OrderDetails getOrderDetailsByCarIdandUserId(long carId, long userId) {
		// TODO Auto-generated method stub

		return orderRepository.getOrderDetailByCarIdandUserId(carId, userId);
	}

	@Override
	public OrderDetails getRentorTripDoneOrder() {
		return orderRepository.getRentorTripDoneOrder();
	}

	@Override
	public OrderDetailsDTO getDTORentorTripDoneOrder() {
		OrderDetails orderRaw = this.getRentorTripDoneOrder();
		if (orderRaw != null) {
			OrderDetailsDTO dto = this.modelMapper.map(orderRaw, OrderDetailsDTO.class);
			Users user = userServices.findById(dto.getUserId());
			dto.setUser(user);
			return dto;
		}
		return null;
	}

	@Override
	public OrderDetailsDTO mapToDTO(int id) {
		OrderDetails car = this.orderRepository.findById(id).get();
		OrderDetailsDTO carDto = this.modelMapper.map(car, OrderDetailsDTO.class);
		Users user = userServices.findById(car.getUserId());
		carDto.setUser(user);
		return carDto;
	}

	@Override
	public void sendOrderEmail(String email, String subject, String content)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

}
