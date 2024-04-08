package com.project.AzCar.Services.Orders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	public void unrespondDetected() {
		List<OrderDetails> orderList = orderRepository.getAll();
		orderList.removeIf(item -> !item.getStatus().equals(Constants.orderStatus.WAITING));
		for (OrderDetails order : orderList) {
			long seconds = Duration.between(order.getCreatedAt(), LocalDateTime.now()).getSeconds();
			CarInfor car = carServices.findById(Integer.parseInt(order.getCarId()));
			long ownerId = car.getCarOwnerId();
			if (seconds >= 120) {
				Violation vio = new Violation();
				vio.setUserId(ownerId);
				vio.setCarId(car.getId());
				vio.setReason(Constants.violations.NO_RESPONSE);
				violationRepo.save(vio);
				order.setStatus(Constants.orderStatus.DECLINED);
				orderRepository.save(order);

				paymentServices.createNewRefund(order.getUserId(), order.getId(), order.getTotalAndFees());
			}
			List<Violation> violations = violationRepo.getByUserAndCarId(ownerId, car.getId());
			if (violations.size() >= 3) {
				car.setStatus(Constants.carStatus.DECLINED);
				carRepo.save(car);
			}
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

}
