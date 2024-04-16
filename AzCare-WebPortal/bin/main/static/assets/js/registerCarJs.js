function backStep1() {
		document.getElementById("one-tab").click();
		scrollToTop();

	}
	function backStep2() {
		document.getElementById("two-tab").click();
		scrollToTop();

	};
	function moveStep3() {
		document.getElementById("three-tab").click();
		scrollToTop();

	};
	function moveStep2() {
		document.getElementById("two-tab").click();
		scrollToTop();

	};	

	function provinceSubmit() {
		const districtSelect = $("#districtSelect").get(0);
		const wardSelect = $("#wardSelect").get(0);
	
		$.ajax({
			url: "/home/carregister/getDistrict",
			type: "get",
			data: { provinceCode: $("#provinceSelect").val() },
			success: function (res) {
				$(districtSelect).removeAttr('disabled');
				$(districtSelect).html('<option value ="">--Select District--</option>');
				$.each(res.districtList, function (key, value) {
					$(districtSelect).append('<option value="' + value.code + '">' + value.full_name + '</option>');
				});
				$(wardSelect).html('<option value="" >--Select Ward--</option>');



			}
		});
	};
	function districtSubmit() {
		
	const districtSelect = $("#districtSelect").get(0);
		const wardSelect = $("#wardSelect").get(0);
	
		$.ajax({
			url: "/home/carregister/getWard",
			type: "get",
			data: { districtCode: $("#districtSelect").val() },
			success: function (res) {
				$(wardSelect).removeAttr('disabled');
				$(wardSelect).html('<option value ="">--Select Ward--</option>');
				$.each(res.wardList, function (key, value) {
					$(wardSelect).append('<option value="' + value.code + '">' + value.full_name + '</option>');
				});
				



			}
		}); 
	};
	function addressSubmit(){
		const houseNo=document.getElementById('houseNo').value;
		const street =document.getElementById('street').value;
		if(houseNo =="" || street ==""){
			document.getElementById('validation-address').textContent = "Address is not valid";
			validateAddress();
			return false;
		}else{
			
		        document.getElementById('validation-address').textContent = "";
		        var address = $("#houseNo").val() + ", "+$("#street").val()+", " + $("#wardSelect option:selected").text() + ", " + $("#districtSelect option:selected").text() + ", " + $("#provinceSelect option:selected").text();
				$("#carAddress").val(address);
				console.log(address);
				console.log($("#carAddress").val());
				
				carAddress.setAttribute("value",address);
				
				offModal.click();
				validateAddress();
	}	
	}
	function brandSubmit() {
		const categorySelect = $("#categorySelect").get(0);
		const modelSelect = $("#modelSelect").get(0);
	
		$.ajax({
			url: "/home/carregister/getCategory",
			type: "get",
			data: { brandName: $("#brandSelect").val() },
			success: function (res) {
				$(categorySelect).removeAttr('disabled');
				$(categorySelect).html('<option value ="">--Select Category--</option>');
				$.each(res.categoryList, function (key, value) {
					$(categorySelect).append('<option value="' + value + '">' + value + '</option>');
				});
				$(modelSelect).html('<option value="" >--Select Model--</option>');



			}
		});
	};
	
	function categorySubmit() {
		const categorySelect = $("#categorySelect").get(0);
		const modelSelect = $("#modelSelect").get(0);

		
		$.ajax({
			url: "/home/carregister/getModel",
			type: "get",
			data: { brandName: $("#brandSelect").val(),cateName: $("#categorySelect").val()},
			success: function (res) {
				$(modelSelect).removeAttr('disabled');
				$(modelSelect).html('<option value ="">--Select Model--</option>');
				$.each(res.modelList, function (key, value) {
					$(modelSelect).append('<option value="' + value + '">' + value + '</option>');
				});
				
			}
		});
		priceRangeHint();
	};
	
	function priceRangeHint(){
		var categorySelected =$("#categorySelect option:selected").text();
		var priceSelected= document.getElementById("priceRent").value;
		if(categorySelected || categorySelected=="--Select Category--" ){
			const count = categorySelected.split(",").length - 1;
			
			if(count ==3){
				document.getElementById('recommendPrice').textContent = "Recommended price for your Car's category : 1000-1500 $/day ";
				if(priceSelected<=1500 &&priceSelected>=1000){
					document.getElementById('recommendPrice').textContent="";
				}
		}
		if(count ==2){
				document.getElementById('recommendPrice').textContent = "Recommended price for your Car's category : 700-999 $/day ";
				if(priceSelected<=999 &&priceSelected>=700){
					document.getElementById('recommendPrice').textContent="";
				}
		}
		
		if(count ==1){
				document.getElementById('recommendPrice').textContent = "Recommended price for your Car's category : 400-699 $/day ";
				if(priceSelected<=699 &&priceSelected>=400){
					document.getElementById('recommendPrice').textContent="";
				}
		}
		if(count ==0){
				document.getElementById('recommendPrice').textContent = "Recommended price for your Car's category : 100-399 $/day ";
				if(priceSelected<=399 &&priceSelected>=100){
					document.getElementById('recommendPrice').textContent="";
				}
		}
		}
		
		
		
		
	}
	function modelSubmit() {
		const categorySelect = $("#categorySelect").get(0);
		const modelSelect = $("#modelSelect").get(0);
		const yearSelect = $("#yearSelect").get(0);
		
		$.ajax({
			url: "/home/carregister/getYear",
			type: "get",
			data: { brandName: $("#brandSelect").val(),cateName: $("#categorySelect").val(),modelName:$("#modelSelect").val()},
			success: function (res) {
				$(yearSelect).removeAttr('disabled');
				$(yearSelect).html('<option value ="">--Select Year--</option>');
				$.each(res.yearList, function (key, value) {
					$(yearSelect).append('<option value="' + value + '">' + value + '</option>');
				});
				
			}
		});
	};
	function yearSubmit() {
		const categorySelect = $("#categorySelect").get(0);
		const modelSelect = $("#modelSelect").get(0);
		const yearSelect = $("#yearSelect").get(0);
		const modelIdSelect = $("#modelIdSelect").get(0);
		
		$.ajax({
			url: "/home/carregister/getModelId",
			type: "get",
			data: { brandName: $("#brandSelect").val(),cateName: $("#categorySelect").val(),modelName:$("#modelSelect").val(),year:$("#yearSelect").val()},
			success: function (res) {
				
					
					$(modelIdSelect).val(res.modelId);
				
				
			}
		});
	};
	
	
		var boxes = document.querySelectorAll(".car-services");
		var numSelected = 0;
		var maxSelected = 18;
		var result =[];
		for (var i = 0; i < boxes.length; i++) {
			boxes[i].addEventListener("click", function() {
				if (this.classList.contains("selected")) {
					this.classList.remove("selected");
					numSelected--;
					result =result.filter(item => item !== this.id);
					resultFeatures.value= result.join(',');
					console.log(result);
					
					
				} else if (numSelected < maxSelected) {
					this.classList.add("selected");
					numSelected++;
					result.push(this.id);
					resultFeatures.value= result.join(',');
					console.log(result);
				}
			});
			
		}
		
		const scrollingElement = (document.scrollingElement || document.body);

		const scrollToBottom = () => {
		   scrollingElement.scrollTop = scrollingElement.scrollHeight;
		}

		const scrollToTop = () => {
		   scrollingElement.scrollTop = 0;
		}
		
		const discountCheck = document.getElementById('discountCheck');
		const discountCheckResponse = document
				.getElementById('discountCheckResponse');

		discountCheck.addEventListener('change', function() {
			if (this.checked) {
				discountCheckResponse.style.display = 'flex';
				discountRange.removeAttribute("disabled");
				document.getElementById('discountValue').textContent = "Discount: "+discountRange.value + "%";
				discountCheckResponse.style.animation = "5s slide-right";
			} else {
				discountCheckResponse.style.animation = "5s slide-left";
				discountCheckResponse.style.display = 'none';
				discountRange.setAttribute("disabled",true);
			}
		});
		
		const deliveryCheck = document.getElementById('deliveryCheck');
		const deliveryResponse = document.getElementById('deliveryResponse');

		deliveryCheck.addEventListener('change', function() {
			if (this.checked) {
				deliveryResponse.style.display = 'flex';
				deliveryDistanceRange.removeAttribute("disabled");
				
				
				document.getElementById('deliveryDistanceValue').textContent = "Delivery Fee: "+deliveryDistanceRange.value + " $";
				
				
				deliveryResponse.style.animation = "5s slide-right";
				
				
			} else {
				deliveryResponse.style.animation = "5s slide-left";
				deliveryResponse.style.display = 'none';
				deliveryDistanceRange.setAttribute("disabled",true);
				
				
				document.getElementById('deliveryDistanceValue').textContent = "";
				
			}
		});
		const limitKmCheck = document.getElementById('limitKmCheck');
		const limitKmResponse = document.getElementById('limitKmResponse');

		limitKmCheck.addEventListener('change', function() {
			if (this.checked) {
				limitKmResponse.style.display = 'flex';
				overLimitFeeRange.removeAttribute("disabled");
				maxKmRange.removeAttribute("disabled");
				document.getElementById('maxKmValue').textContent = "Cleanning Fee: "+maxKmRange.value + " $";
				document.getElementById('overLimitFeeValue').textContent = "Decoration Fee: "+overLimitFeeRange.value + " $";
				limitKmResponse.style.animation = "5s slide-right";
			} else {
				limitKmResponse.style.animation = "5s slide-left";
				limitKmResponse.style.display = 'none';
				overLimitFeeRange.setAttribute("disabled",true);
				maxKmRange.setAttribute("disabled",true);
				document.getElementById('maxKmValue').textContent = "";
				document.getElementById('overLimitFeeValue').textContent = "";
			}
		});
		const fuelCheck = document.getElementById('fuelCheck');
		const fuelResponse = document.getElementById('fuelResponse');

		fuelCheck.addEventListener('change', function() {
			if (this.value == "Gasoline"|| this.value =="Diesel") {
				fuelResponse.style.display = 'block';
				fuelResponse.style.animation = "5s slide-right";
				for(var item of document.getElementById('transmissionCheck').querySelectorAll('option')){
						
				item.hidden= false;
				}
			}else if(this.value == "Electric"){
				document.getElementById('validation-result-final').textContent = "";
				fuelResponse.style.display = 'block';
				
				document.getElementById('transmissionCheck').value= "1";
				for(var item of document.getElementById('transmissionCheck').querySelectorAll('option')){
					if(item.value != "1"){
						
						item.hidden= true;
					}
				}
				
			} 
			else {
				fuelResponse.style.animation = "5s slide-left";
				fuelResponse.style.display = 'none';
			}
		});
		
		var rangeInput = document.getElementById('discountRange');
		var valueSpan = document.getElementById('discountValue');
		var deliveryDistanceRange = document.getElementById('deliveryDistanceRange');
		var deliveryDistanceValue = document.getElementById('deliveryDistanceValue');


		var deliveryFeeValue = document.getElementById('deliveryFeeValue');
		var freeDeliveryValue = document.getElementById('freeDeliveryValue');
		var maxKmRange = document.getElementById('maxKmRange');
		var maxKmValue = document.getElementById('maxKmValue');
		var overLimitFeeRange = document.getElementById('overLimitFeeRange');
		var overLimitFeeValue = document.getElementById('overLimitFeeValue');
	
		rangeInput.addEventListener('input', function() {
		    valueSpan.textContent = "Discount: "+this.value + "%";
		});
		deliveryDistanceRange.addEventListener('input', function() {
			deliveryDistanceValue.textContent = "Delivery Fee: "+this.value + " $";
		});
		
		
		maxKmRange.addEventListener('input', function() {
			maxKmValue.textContent = "Cleanning Fee: "+this.value + " $";
		});
		overLimitFeeRange.addEventListener('input', function() {
			overLimitFeeValue.textContent = "Decoration Fee: "+this.value + " $";
		});
		
		
		
	function validateCarbasic(){
		const modelIdSelect = document.getElementById("modelIdSelect").value;
		const carSeatQty = document.getElementById("car-seat-qty").value;
		const transmissionCheck = document.getElementById("transmissionCheck").value;
		
		if(carSeatQty >7 || carSeatQty<2){
			document.getElementById('validation-numberOfSeat-result').textContent = "Seat quantity not valid";
		}else{
			document.getElementById('validation-numberOfSeat-result').textContent = "";
		}
		if(modelIdSelect == "" ||carSeatQty =="" || transmissionCheck ==""){
			document.getElementById('validation-result-final').textContent = "Car basic infor must be declared";
		}else{
			
		        document.getElementById('validation-result-final').textContent = "";
		  
		}
		
	}
	
	function carDesciptionCheck(){
		const carDescription = document.getElementById("car-description").value;
		if(carDescription == ""){
			document.getElementById('validation-carDescription').textContent = "Car description must be declared";
		}else{
			
		        document.getElementById('validation-carDescription').textContent = "";
	}
	}

	function priceRentCheck(){
		priceRangeHint();
		const priceRent = document.getElementById("priceRent").value;
		if(priceRent <1 ||priceRent % 1 !== 0){
			document.getElementById('validation-priceRent').textContent = "Price is not valid";
		}else{
			
		        document.getElementById('validation-priceRent').textContent = "";
	}
	}
	
	function ruleCheck(){
		const rules = document.getElementById("rules").value;
		if(rules ==""){
			document.getElementById('validation-rules').textContent = "Rule must be declared";
		}else{
			
		        document.getElementById('validation-rules').textContent = "";
	}
	};
	function validatePlate(){
		const plateNumber = document.getElementById("car-plate-number").value;
		var pattern = /\b\d{2}[A-Z]{1}-\d{4,5}\b/;
		if(plateNumber==""){
			document.getElementById('validation-plate').textContent = "Plate License must be declared";
		}
		else if(!pattern.test(plateNumber)){
			document.getElementById('validation-plate').textContent = "Plate License is not valid";
		}else{
			
		        document.getElementById('validation-plate').textContent = "";
	}
	}
	validateCarbasic();
	carDesciptionCheck();
	priceRentCheck();
	ruleCheck();
	validatePlate();
	validateImg();
	validateAddress();
	function validateImg(){
		var frontImg =document.getElementById('frontImg').value;
		var behindImg =document.getElementById('behindImg').value;
		var leftImg =document.getElementById('leftImg').value;
		var rightImg =document.getElementById('rightImg').value;
		var insideImg =document.getElementById('insideImg').value;
		
		
		if(frontImg ==""|| behindImg=="" ||leftImg=="" || rightImg==""||insideImg==""){
			document.getElementById('validation-NUll-img').textContent = "All neccessary images must be uploaded";
		}
		else{
			document.getElementById('validation-NUll-img').textContent = "";
		}
		if(frontImg==behindImg || frontImg==leftImg||frontImg==rightImg||frontImg==insideImg||behindImg==leftImg||behindImg==rightImg||behindImg==insideImg||leftImg==rightImg||leftImg==insideImg||rightImg==insideImg){
        	console.log($('#frontImg').val())
			document.getElementById('validation-Duplicate-image').textContent = "Some Images are duplicated name, Please try again";
		}else{
			document.getElementById('validation-Duplicate-image').textContent = "";
		}
	}
	function previewImage(inputId, previewId){
		
		validateImg();
		var input = document.getElementById(inputId);
        var preview = document.getElementById(previewId);
        var value =input.value;
        
		if (!value.match(/\.(jpg|jpeg|png|gif)$/i)){
		
			input.setAttribute('value', '');
			preview.setAttribute('src', '');
			document.getElementById('validation-image').textContent = "Some Images are not valid";
		}else{
			
		        document.getElementById('validation-image').textContent = "";
		        if (input && input.files && input.files[0]) {
		            var reader = new FileReader();

		            reader.onload = function (e) {
		                preview.src = e.target.result;
		            }

		            reader.readAsDataURL(input.files[0]);
		        }
			}
        }
	function validateAddress(){
		
		var addressResult= document.getElementById('carAddress').value;
		
		if(addressResult==""){    
	
			document.getElementById('validation-NULL-address').textContent = "Car address must be declared";
		}else{
			document.getElementById('validation-NULL-address').textContent = "";
		}
		
	}
	function checkSubmitRegister(){
		validateImg();
		validateAddress();
		var plateCheck= document.getElementById('validation-plate').textContent.trim().length;
		var carBasicCheck = document.getElementById('validation-result-final').textContent.trim().length;
		var descriptionCheck =document.getElementById('validation-carDescription').textContent.trim().length;
		var priceCheck =document.getElementById('validation-priceRent').textContent.trim().length;
		var addressCheck = document.getElementById('carAddress').textContent.trim().length;
		
		var ruleCheck =document.getElementById('validation-rules').textContent.trim().length;
		var ImgCheck =document.getElementById('validation-image').textContent.trim().length;
		var ImgCheckNull =document.getElementById('validation-NUll-img').textContent.trim().length;
		var ImgChecDuplicate =document.getElementById('validation-Duplicate-image').textContent.trim().length;
		var AddressCheckNull = document.getElementById('validation-NULL-address').textContent.trim().length;
		var recommendPriceCheckNull = document.getElementById('recommendPrice').textContent.trim().length;
		const sum = plateCheck+carBasicCheck+descriptionCheck+priceCheck+addressCheck+ruleCheck+ImgCheck+ImgCheckNull+ImgChecDuplicate+AddressCheckNull+recommendPriceCheckNull;
		if(sum>0){
			console.log(sum);
			document.getElementById('toast-failed-register-car').click();
			return false;
		}else{
			document.getElementById('toast-success-registered-car').click();
			var pageLoader = document.querySelector('.page_loader');
			pageLoader.style.display = 'block';
			return registerRentCar.submit();
		}
	}
	function hintDescriptionSubmit(){
		var carDescription = document.getElementById("car-description");
		var hint = document.getElementById("hintDescriptionSelect").value;
		
		carDescription.value = hint;
		carDesciptionCheck();
	}
	
	function hintRuleSubmit(){
		var carRule = document.getElementById("rules");
		var hint = document.getElementById("hintRuleSelect").value;
		
		carRule.value = hint;
		ruleCheck();
	}
	
	