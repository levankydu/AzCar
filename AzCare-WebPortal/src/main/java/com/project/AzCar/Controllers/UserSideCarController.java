package com.project.AzCar.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AzCar.Entities.Locations.City;
import com.project.AzCar.Entities.Locations.District;
import com.project.AzCar.Entities.Locations.Ward;
import com.project.AzCar.Services.Cars.BrandServices;
import com.project.AzCar.Services.Locations.DistrictServices;
import com.project.AzCar.Services.Locations.ProvinceServices;
import com.project.AzCar.Services.Locations.WardServices;


@Controller
public class UserSideCarController {

	@Autowired
	private BrandServices brandServices;
	
	@Autowired
	private ProvinceServices provinceServices;
	
	@Autowired
	private  DistrictServices districtServices;
	
	@Autowired
	private WardServices wardServices;
	@GetMapping("/home/carregister/")
	public String getCarRegisterPage(Model brandList, Model provinceList) {
		
		List<String> brands = brandServices.getBrandList();
		List<City> provinces = provinceServices.getListCity();
		brandList.addAttribute("brandList",brands);
		provinceList.addAttribute("provinceList", provinces);
		return "registerCar";
	}
	
	@GetMapping("/home/carregister/getCategory")
	public ResponseEntity<?> getCategory(@RequestParam("brandName") String brandName) {
        List<String> categoryList = brandServices.getCategoryListByBrand(brandName);

        return ResponseEntity.ok().body(Map.of("categoryList", categoryList));
    }
	
	@GetMapping("/home/carregister/getModel")
	public ResponseEntity<?> getModel(@RequestParam("brandName") String brandName,@RequestParam("cateName") String cateName) {
        List<String> modelList = brandServices.getModelListByCateAndBrand(brandName,cateName);

        return ResponseEntity.ok().body(Map.of("modelList", modelList));
    }
	@GetMapping("/home/carregister/getYear")
	public ResponseEntity<?> getYear(@RequestParam("brandName") String brandName,@RequestParam("cateName") String cateName,@RequestParam("modelName") String modelName) {
        List<String> yearList = brandServices.getYear(brandName, cateName, modelName);

        return ResponseEntity.ok().body(Map.of("yearList", yearList));
    }
	@GetMapping("/home/carregister/getModelId")
	public ResponseEntity<?> getModelId(@RequestParam("brandName") String brandName,@RequestParam("cateName") String cateName,@RequestParam("modelName") String modelName,@RequestParam("year") String year) {
        String modelId = brandServices.getModelId(brandName, cateName, modelName,Integer.parseInt(year));

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
