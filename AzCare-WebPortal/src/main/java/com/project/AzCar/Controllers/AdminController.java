package com.project.AzCar.Controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import com.project.AzCar.Dto.Categories.CategoriesDto;
import com.project.AzCar.Entities.Cars.BrandImages;
import com.project.AzCar.Entities.Cars.CarModelList;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Cars.BrandImageServices;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.UploadFiles.FilesStorageServices;
import com.project.AzCar.Services.Users.UserServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class AdminController {

	@Autowired

	private BrandServices brandServices;
	@Autowired
	BrandImageServices brandImageServices;

	@Autowired
	private FilesStorageServices fileStorageServices;
	@Autowired
	private UserServices userRepo;


	@GetMapping("/dashboard/")
	public String getDashboard(Model model, Authentication authentication) {
Users loginedUser = new Users();

		loginedUser.setFirstName(authentication.getName());
		model.addAttribute("user", loginedUser);
		return "admin/dashboard";
	}

	@GetMapping("/dashboard/brands/")
	public String getBrandPage(Model brandsData, Model cateData, Model sessionUpdateBrandLogo,Model createdCarModel,
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
			createdCarModel.addAttribute("created_carModel",
					request.getSession().getAttribute("created_carModel"));
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

	@GetMapping("/dashboard/ListAccount")
	public String getfindAll(Model model) {

		List<Users> userlists = userRepo.findAllUsers();
		model.addAttribute("userlists", userlists);
		return "admin/ListAccount";
	}

	
	

}
