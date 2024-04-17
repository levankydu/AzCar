function validateImg() {
	var frontImg = document.getElementById('frontImg').value;
	var behindImg = document.getElementById('behindImg').value;

	if (frontImg == "" || behindImg == ""
		&& document.getElementById('validation-NUll-img')) {

		document.getElementById('validation-NUll-img').textContent = "All neccessary images must be uploaded";
	} else {

		if (document.getElementById('validation-NUll-img')) {
			document.getElementById('validation-NUll-img').textContent = "";
		}

	}
	if (frontImg == behindImg
		&& document.getElementById('validation-NUll-img')
		&& document.getElementById('validation-Duplicate-image')) {

		document.getElementById('validation-Duplicate-image').textContent = "Images are duplicated name, Please try again";
	} else {

		if (document.getElementById('validation-Duplicate-image')) {
			document.getElementById('validation-Duplicate-image').textContent = "";
		}
	}

}
function previewImagePlates(inputId, previewId) {

	validateImg();
	var input = document.getElementById(inputId);
	var preview = document.getElementById(previewId);
	var value = input.value;

	if (!value.match(/\.(jpg|jpeg|png|gif)$/i)) {

		input.setAttribute('value', '');
		preview.setAttribute('src', '');
		document.getElementById('validation-image').textContent = "Some Images are not valid";
	} else {

		document.getElementById('validation-image').textContent = "";
		if (input && input.files && input.files[0]) {
			var reader = new FileReader();

			reader.onload = function(e) {
				preview.src = e.target.result;
			}

			reader.readAsDataURL(input.files[0]);
		}
	}
}
validateImg();
function checkImg() {
	validateImg();

	var ImgCheck = document.getElementById('validation-image').textContent
		.trim().length;
	var ImgCheckNull = document.getElementById('validation-NUll-img').textContent
		.trim().length;
	var ImgChecDuplicate = document
		.getElementById('validation-Duplicate-image').textContent
		.trim().length;

	const sum = ImgCheck + ImgCheckNull + ImgChecDuplicate;
	if (sum > 0) {
		console.log(sum);
		document.getElementById('toast-failed-uploadDriveLicense').click();
		return false;
	} else {
		document.getElementById('loadding-addLicense').click();
		setTimeout(function() { uploadDriveLicense.submit() }, 8000);

	}

}
function descriptionSubmit() {
	var imgShow = document.getElementById('imgShow');
	var description = document.getElementById('description');
	if (description.value != "") {
		imgShow.classList.remove('d-none');
	}
	else {
		imgShow.classList.add('d-none');
	}



}
function previewImage() {
	var input = document.getElementById("frontImg");
	var preview = document.getElementById("frontImgPre");

	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {

			preview.src = e.target.result;
		};

		reader.readAsDataURL(input.files[0]);
	} else {
		preview.src = "";
	}
}

function checkNewDiscount(modalId) {

	var modal = document.getElementById(modalId);
	if (!modal) {
		console.error("Modal not found!");
		return; // Thoát khỏi hàm nếu modal không tồn tại
	}
	var newDiscountValidateId = "newDiscountValidate-" + modalId;
	var newDiscountId = "newDiscount-" + modalId;
	var newDiscount = document.getElementById(newDiscountId).value;
	var validationDiscountElement = document
		.getElementById(newDiscountValidateId);
	var validationMessage = "";

	if (newDiscount == 0) {
		validationMessage = "New discount must be valid numbers.";
	}

	if (newDiscount < 0 || newDiscount % 1 !== 0) {
		validationMessage = "New discount must be positive integer.";

	}
	if (newDiscount > 100) {
		validationMessage = "New discount must be lower than 100%.";
	}

	if (validationMessage !== "") {
		validationDiscountElement.innerHTML = validationMessage;
	} else {
		validationDiscountElement.innerHTML = "";
	}
}
function hintPrice(modalId) {
	var modal = document.getElementById(modalId);
	if (!modal) {
		console.error("Modal not found!");
		return;
	}

	var newPriceId = "newPrice-" + modalId;
	var carCateId = "carCate-" + modalId;
	var category = document.getElementById(carCateId).value;
	var hintMessage = "";
	var recommendPriceId = "recommendPrice-" + modalId;
	var hintMessageElement = document.getElementById(recommendPriceId);

	const count = category.split(",").length - 1;
	var priceSelected = parseFloat(document.getElementById(newPriceId).value);
	if (count == 3) {
		hintMessageElement.textContent = "Recommended price for your Car's category : 1000-1500 $/day ";
		if (priceSelected <= 1500 && priceSelected >= 1000) {
			hintMessageElement.textContent = "";
		}
	}
	if (count == 2) {
		hintMessageElement.textContent = "Recommended price for your Car's category : 700-999 $/day ";
		if (priceSelected <= 999 && priceSelected >= 700) {
			hintMessageElement.textContent = "";
		}
	}

	if (count == 1) {
		hintMessageElement.textContent = "Recommended price for your Car's category : 400-699 $/day ";
		if (priceSelected <= 699 && priceSelected >= 400) {
			hintMessageElement.textContent = "";
		}
	}
	if (count == 0) {
		hintMessageElement.textContent = "Recommended price for your Car's category : 100-399 $/day ";
		if (priceSelected <= 399 && priceSelected >= 100) {
			hintMessageElement.textContent = "";
		}
	}

}

function checkNewPrice(modalId) {

	hintPrice(modalId);
	var modal = document.getElementById(modalId);
	if (!modal) {
		console.error("Modal not found!");
		return;
	}
	var newPriceId = "newPrice-" + modalId;

	var newPrice = parseFloat(document.getElementById(newPriceId).value);
	var validationMessage = "";
	var newPriceValidate = "newPriceValidate-" + modalId;
	var validationNewPriceElement = document
		.getElementById(newPriceValidate);

	if (newPrice == 0 || newPrice == null) {
		validationMessage = "New Price must be valid numbers.";
	}

	if (newPrice <= 0 || newPrice % 1 !== 0) {
		validationMessage = "New price must be positive number.";
	}
	if (validationMessage !== "") {
		validationNewPriceElement.innerHTML = validationMessage;

	} else {
		validationNewPriceElement.innerHTML = "";
	}
}
function submitValidation(carId) {
	checkNewPrice(carId);
	hintPrice(carId);
	checkNewDiscount(carId);
	var formId = "updateCar-" + carId;
	var form = document.getElementById(formId);
	var check1Id = "newPriceValidate-" + carId;
	var check2Id = "newDiscountValidate-" + carId;
	var check3Id = "recommendPrice-" + carId;
	var check1 = document.getElementById(check1Id).innerHTML;
	var check2 = document.getElementById(check2Id).innerHTML;
	var check3 = document.getElementById(check3Id).innerHTML;
	if (check1 === '' && check2 === '' && check3 === '') {
		return form.submit();
	} else {
		document.getElementById("toast-failed-updated-car").click();

		return false;
	}
}

var checkDriverLicense = document.getElementById('driverLicenseResult').value;
if (checkDriverLicense == "false"){
	document.getElementById("addLicense-false").click();
}
else if (checkDriverLicense == "success") {
	document.getElementById("addLicense-success").click();
}

function toggleRule1(){
	var rule = document.getElementById('rule1');

        if (rule.style.display === 'none' || rule.style.display === '') {
            rule.style.display = 'block';
        } else {
            rule.style.display = 'none';
        }
    }

function toggleRule2(){
	var rule = document.getElementById('rule2');

        if (rule.style.display === 'none' || rule.style.display === '') {
            rule.style.display = 'block';
        } else {
            rule.style.display = 'none';
        }
	
}
function toggleRule3(){
	var rule = document.getElementById('rule3');

        if (rule.style.display === 'none' || rule.style.display === '') {
            rule.style.display = 'block';
        } else {
            rule.style.display = 'none';
        }
	
}
function toggleRule4(){
	var rule = document.getElementById('rule4');

        if (rule.style.display === 'none' || rule.style.display === '') {
            rule.style.display = 'block';
        } else {
            rule.style.display = 'none';
        }
	
}