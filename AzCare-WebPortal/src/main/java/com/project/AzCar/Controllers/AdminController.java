package com.project.AzCar.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.project.AzCar.Dto.Brands.BrandsDto;
import com.project.AzCar.Dto.CarInfos.CarInforDto;
import com.project.AzCar.Dto.Categories.CategoriesDto;
import com.project.AzCar.Dto.PlateVerify.PlateVerifyDto;
import com.project.AzCar.Entities.Cars.BrandImages;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Cars.CarModelList;
import com.project.AzCar.Entities.Cars.PlateImages;
import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBooking;
import com.project.AzCar.Entities.ServiceAfterBooking.ServiceAfterBookingDTO;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Notification.Message;
import com.project.AzCar.Repositories.ServiceAfterBooking.ServiceBookingRepositories;
import com.project.AzCar.Services.Cars.BrandImageServices;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Cars.CarImageServices;
import com.project.AzCar.Services.Cars.CarServices;
import com.project.AzCar.Services.Cars.ExtraFeeServices;
import com.project.AzCar.Services.Cars.PlateImageServices;
import com.project.AzCar.Services.Cars.PlusServiceServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Orders.OrderDetailsService;
import com.project.AzCar.Services.Payments.PaymentService;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

	@Autowired
	private BrandServices brandServices;
	@Autowired
	private OrderDetailsService orderServices;
	@Autowired
	BrandImageServices brandImageServices;
	@Autowired
	private FilesStorageServices fileStorageServices;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ProvinceServices provinceServices;
	@Autowired
	private CarImageServices carImageServices;
	@Autowired
	private ExtraFeeServices extraFeeServices;
	@Autowired
	private PlusServiceServices plusServiceServices;
	@Autowired
	private CarServices carServices;
	@Autowired
	private UserServices userServices;
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private PlateImageServices plateImageServices;
	@Autowired
	private ServiceBookingRepositories afterBookingRepositories;
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/dashboard/")
	public String getDashboard(Model model, Authentication authentication) {
		Users loginedUser = new Users();

		loginedUser.setFirstName(authentication.getName());
		model.addAttribute("user", loginedUser);
		return "admin/dashboard";
	}

	@GetMapping("/dashboard/platesVerify/")
	public String getPlatesVerifyPage(Model ModelView) {
		List<Long> listUserId = plateImageServices.getUserIdList();
		List<PlateImages> listImg = plateImageServices.getAll();
		List<PlateVerifyDto> listPlateDto = new ArrayList<>();

		for (var item : listUserId) {
			var PlateVerifyDto = new PlateVerifyDto();
			PlateVerifyDto.setUserModel(userServices.findById(item));
			List<PlateImages> userImages = listImg.stream().filter(img -> img.getUserId() == item)
					.collect(Collectors.toList());
			PlateVerifyDto.setPlateImages(userImages);
			listPlateDto.add(PlateVerifyDto);
		}

		ModelView.addAttribute("listPlates", listPlateDto);
		return "admin/verifyPlates";

	}

	@PostMapping("/dashboard/platesVerfy/")
	public String confirmPlateVerify(@ModelAttribute("status") String status, @ModelAttribute("userId") String userId) {

		var user = userServices.findById(Long.parseLong(userId));
		List<PlateImages> list = plateImageServices.getAll();
		List<PlateImages> userImages = list.stream().filter(img -> img.getUserId() == Long.parseLong(userId))
				.collect(Collectors.toList());

		if (status.equals("accepted")) {
			for (var item : userImages) {
				item.setStatus(Constants.plateStatus.ACCEPTED);
				plateImageServices.save(item);
			}

			try {
				sendEmailAcceptPlate(user.getEmail());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (status.equals("declined")) {
			for (var item : userImages) {
				item.setStatus(Constants.plateStatus.DECLINED);
				plateImageServices.save(item);
			}

			try {
				sendEmailDeclinePlate(user.getEmail());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return "redirect:/dashboard/platesVerify/";
	}

	@GetMapping("/dashboard/carverify/")
	public String getCarVerifyPage(Model ModelView) {

		List<CarInfor> list = carServices.findAll();
		List<CarInforDto> listDto = new ArrayList<>();
		List<String> brands = brandServices.getBrandList();
		List<String> categories = brandServices.getCategoryList();
		List<String> listProvince = provinceServices.getListCityString();
		List<City> provinces = provinceServices.getListCity();
		for (var item : list) {

			var itemDto = carServices.mapToDto(item.getId());
			itemDto.setCarmodel(brandServices.getModel(item.getModelId()));
			itemDto.setImages(carImageServices.getImgByCarId(item.getId()));
			itemDto.setOwner(userServices.findById(item.getCarOwnerId()));
			for (var c : listProvince) {
				if (item.getAddress().contains(c)) {
					itemDto.setAddress(c);
				}
			}

			listDto.add(itemDto);

		}
		ModelView.addAttribute("listBrand", brands);
		ModelView.addAttribute("listCategory", categories);
		ModelView.addAttribute("provinceList", provinces);
		ModelView.addAttribute("carRegisterList", listDto);
		return "admin/carverify";

	}

	@GetMapping("/dashboard/carverify/{carId}")
	public String getVerifyDetailsPage(@PathVariable("carId") String carId, Model carDetails, Model checkPlate) {
		var listAcceptedCar = carServices.findAll();

		var model = carServices.findById(Integer.parseInt(carId));
		for (var item : listAcceptedCar) {
			if (model.getLicensePlates().equals(item.getLicensePlates())) {
				checkPlate.addAttribute("checkPlate", "Duplicate License Plate");
			}
		}

		var modelDto = carServices.mapToDto(model.getId());
		modelDto.setCarmodel(brandServices.getModel(model.getModelId()));
		modelDto.setImages(carImageServices.getImgByCarId(model.getId()));
		modelDto.setOwner(userServices.findById(model.getCarOwnerId()));
		if (model.isCarPlus()) {
			modelDto.setCarPlusModel(plusServiceServices.findByCarId(model.getId()));
		}

		if (model.isExtraFee()) {

			modelDto.setExtraFeeModel(extraFeeServices.findByCarId(model.getId()));
		}
		checkPlate.addAttribute("checkPlate", "");
		carDetails.addAttribute("carDetails", modelDto);

		return "admin/verifyDetails";
	}

	@GetMapping("/dashboard/brands/")
	public String getBrandPage(Model brandsData, Model cateData, Model sessionUpdateBrandLogo, Model createdCarModel,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<String> brands = brandServices.getBrandList();
		List<BrandsDto> brandList = new ArrayList<>();
		List<String> categories = brandServices.getCategoryList();
		List<CategoriesDto> categoryList = new ArrayList<>();
		for (int i = 0; i < brands.size(); i++) {
			BrandsDto brandsDto = new BrandsDto();
			brandsDto.setBrandName(brands.get(i));
			String brandUrl = MvcUriComponentsBuilder.fromMethodName(AdminController.class, "getImage",
					brandImageServices.getBrandImgUrl(brands.get(i)).toString()).build().toString();
			if (brandUrl.contains("null")) {
				brandsDto.setBrandLogo("null");
			} else {
				brandsDto.setBrandLogo(brandUrl);
			}

			brandsDto.setNumberOfCars((long) brandServices.getCarsListByBrand(brands.get(i)).size());
			brandList.add(brandsDto);
		}
		for (int i = 0; i < categories.size(); i++) {
			CategoriesDto cateDto = new CategoriesDto();
			cateDto.setCateName(categories.get(i));
			cateDto.setNumberOfCars((long) brandServices.getCarsListByCategory(categories.get(i)).size());
			categoryList.add(cateDto);
		}
		if (request.getSession().getAttribute("update_brandLogo") != null) {
			sessionUpdateBrandLogo.addAttribute("update_brandLogo",
					request.getSession().getAttribute("update_brandLogo"));
			request.getSession().removeAttribute("update_brandLogo");

		}
		if (request.getSession().getAttribute("created_carModel") != null) {
			createdCarModel.addAttribute("created_carModel", request.getSession().getAttribute("created_carModel"));
			request.getSession().removeAttribute("created_carModel");

		}

		cateData.addAttribute("categoryList", categoryList);
		brandsData.addAttribute("brandsList", brandList);

		return "admin/brands";
	}

	@PostMapping("/dashboard/brands/addNewModel")
	public String addNewModel(@ModelAttribute("carModel") CarModelList carModel, BindingResult bindingResult,
			HttpServletRequest request) {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		if (bindingResult.hasErrors()) {
			// Handle validation errors
			return "admin/brands" + "?error";
		} else {
			carModel.setObjectId(generatedString);

			brandServices.saveBrand(carModel);
			request.getSession().setAttribute("created_carModel", "done");
			return "redirect:/dashboard/brands/";
		}

	}

	@GetMapping("/dashboard/platesVerfy/{filename}")
	public ResponseEntity<Resource> getPlateImage(@PathVariable("filename") String filename) {
		List<Users> list = userServices.findAllUsers();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/userImages/" + list.get(i).getId() + "-"
					+ list.get(i).getEmail().replace(".", "-").replace("@", "-");
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

	@GetMapping("/dashboard/brands/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
		Resource file = fileStorageServices.load(filename, Constants.ImgDir.BRAND_DIR);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/dashboard/brands/updateBrandLogo/{brandName}")
	public String getUpdateBrandLogoPage(@PathVariable("brandName") String brandName, Model model, Model brand,
			Model sessionUpdateBrandLogo, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		model.addAttribute("brandImages", new BrandImages());
		brand.addAttribute("brandName", brandName);

		if (request.getSession().getAttribute("update_brandLogo") != null) {
			sessionUpdateBrandLogo.addAttribute("update_brandLogo",
					request.getSession().getAttribute("update_brandLogo"));
			request.getSession().removeAttribute("update_brandLogo");

		}

		return "admin/updateBrandLogo";
	}

	@PostMapping("/dashboard/brands/updateBrandLogo/{brandName}")
	public String uploadImage(Model model, @PathVariable("brandName") String brandName,
			@RequestParam("image") MultipartFile image, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String message = "";
		BrandImages newBrandImages = new BrandImages();

		try {
			fileStorageServices.save(image, Constants.ImgDir.BRAND_DIR);
			message = "Uploaded the image successfully: " + image.getOriginalFilename();
			model.addAttribute("message", message);
			newBrandImages.setBrandName(brandName);
			newBrandImages.setImageUrl(image.getOriginalFilename());
			brandImageServices.saveImage(newBrandImages);
			request.getSession().setAttribute("update_brandLogo", "done");
			return "redirect:/dashboard/brands/";

		} catch (Exception e) {
			message = "Could not upload the image: " + image.getOriginalFilename() + ". Error: " + e.getMessage();
			model.addAttribute("message", message);
		}

		request.getSession().setAttribute("update_brandLogo", "error");
		return "redirect:/dashboard/brands/updateBrandLogo/" + brandName + "?error";

	}

	@GetMapping("/dashboard/carverify/{carId}/{filename}")
	public ResponseEntity<Resource> getDetailsImage(@PathVariable("carId") String carId,
			@PathVariable("filename") String filename) {
		var model = carServices.findById(Integer.parseInt(carId));
		String dir = "./UploadFiles/carImages/" + model.getModelId() + "-" + model.getId();
		Resource file = fileStorageServices.load(filename, dir);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/dashboard/confirmCarverify")
	public String verifyCar(@ModelAttribute("status") String status, @ModelAttribute("carId") String carId) {

		var model = carServices.findById(Integer.parseInt(carId));
		var modelDto = carServices.mapToDto(model.getId());
		modelDto.setOwner(userServices.findById(model.getCarOwnerId()));
		modelDto.setCarmodel(brandServices.getModel(model.getModelId()));

		if (status.equals("accepted")) {
			model.setStatus(Constants.carStatus.READY);
			carServices.saveCarRegister(model);

			try {
				sendEmailAccept(modelDto.getOwner().getEmail(), modelDto);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (status.equals("declined")) {
			model.setStatus(Constants.carStatus.DECLINED);
			carServices.saveCarRegister(model);
			try {
				sendEmailDecline(modelDto.getOwner().getEmail(), modelDto);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return "redirect:/dashboard/carverify/" + carId;
	}

	@GetMapping("/dashboard/ListAccount")
	public String getfindAll(Model model) {
		List<Users> userlists = userServices.findAllUsers();
		model.addAttribute("userlists", userlists);
		return "admin/ListAccount";
	}

	@GetMapping("/dashboard/ClientService")
	public String clientService(Model ModelView) {
		List<ServiceAfterBooking> list = afterBookingRepositories.findAll();
		List<ServiceAfterBookingDTO> listDTO = new ArrayList<>();
		for (var item : list) {
			var car = carServices.mapToDto(item.getCarId());
			var itemDto = carServices.afterServiceMapToDto(item.getId());
			var orderDto = orderServices.mapToDTO(item.getOrderId());
			itemDto.setCar(car);
			itemDto.setOrder(orderDto);
			listDTO.add(itemDto);
		}
		ModelView.addAttribute("list", listDTO);
		return "admin/tuReview";
	}

	@GetMapping("/dashboard/ClientService/img/{filename}")
	public ResponseEntity<Resource> getTuImage(@PathVariable("filename") String filename) throws IOException {
		List<ServiceAfterBooking> list = afterBookingRepositories.findAll();
		String dir = "";
		int i = 0;
		while (i < list.size()) {
			dir = "./UploadFiles/tuImages" + "/" + list.get(i).getCarId() + "-" + list.get(i).getId();
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

	@PostMapping("/dashboard/ClientService/")
	public String postTuReview(@ModelAttribute("status") String status, @ModelAttribute("orderId") String orderId) {

		System.out.println(orderId);
		System.out.println(status);
		if (status.equals("accepted")) {
			ServiceAfterBooking model = afterBookingRepositories.findById(Integer.parseInt(orderId)).get();
			model.setStatus("Accepted");
			afterBookingRepositories.save(model);
		}
		if (status.equals("declined")) {
			ServiceAfterBooking model = afterBookingRepositories.findById(Integer.parseInt(orderId)).get();
			model.setStatus("Declined");
			afterBookingRepositories.save(model);
		}

		return "redirect:/dashboard/ClientService";
	}

	private void sendEmailAccept(String email, CarInforDto carDetails)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Successfull register your car";
		String content = "<p>Hello," + email + "</p>" + "<p>Thank you for registering your car rental with AzCar.</p>"
				+ "<p>Below are some main details of your car:</p>" + "<p><b>Car Details:</b></p>" + "<p>" + "Brand: "
				+ carDetails.getCarmodel().getBrand() + "</p>" + "<p>" + "Model: " + carDetails.getCarmodel().getModel()
				+ "</p>" + "<p>" + "Price: " + carDetails.getPrice() + " $/day" + "</p>" + "<p>" + "License Plates: "
				+ carDetails.getLicensePlates() + "</p>" + "<p>" + "Pick-up Location: " + carDetails.getAddress()
				+ "</p>" +

				"<p>This is to confirm that we already verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	private void sendEmailDecline(String email, CarInforDto carDetails)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Failed verify register your car";
		String content = "<p>Hello," + email + "</p>" + "<p>Thank you for registering your car rental with AzCar.</p>"
				+ "<p>Below are some main details of your car:</p>" + "<p><b>Car Details:</b></p>" + "<p>" + "Brand: "
				+ carDetails.getCarmodel().getBrand() + "</p>" + "<p>" + "Model: " + carDetails.getCarmodel().getModel()
				+ "</p>" + "<p>" + "Price: " + carDetails.getPrice() + " $/day" + "</p>" + "<p>" + "License Plates: "
				+ carDetails.getLicensePlates() + "</p>" + "<p>" + "Pick-up Location: " + carDetails.getAddress()
				+ "</p>" +

				"<p>This is to confirm that your car is not meet our rules</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	private void sendEmailAcceptPlate(String email)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Successfull verify your License Plate";
		String content = "<p>Hello," + email + "</p>"
				+ "<p>Thank you for registering your License Plate with AzCar.</p>" +

				"<p>This is to confirm that we already verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	private void sendEmailDeclinePlate(String email)
			throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Verify your License Plate not succesfully";
		String content = "<p>Hello," + email + "</p>" + "<p>Sorry,Your License Plate is not verified with AzCar.</p>" +

				"<p>This is to confirm that we already verify your information</p>"
				+ "<p>For any further assistance, feel free to contact us.</p>" + "<p>Best regards,<br>AzCar Team</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	// Mapped as /app/application
	@MessageMapping("/application")
	@SendTo("/all/messages")
	public Message send(final Message message) throws Exception {
		return message;
	}

	// Mapped as /app/private
	@MessageMapping("/private")
	public void sendToSpecificUser(@Payload Message message) throws Exception {
		simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
	}
}
