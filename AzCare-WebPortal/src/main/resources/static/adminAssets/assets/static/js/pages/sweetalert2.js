const Swal2 = Swal.mixin({
	customClass: {
		input: 'form-control'
	}
})

const Toast = Swal.mixin({
	toast: true,
	position: 'top-end',
	showConfirmButton: false,
	timer: 3000,
	timerProgressBar: true,
	didOpen: (toast) => {
		toast.addEventListener('mouseenter', Swal.stopTimer)
		toast.addEventListener('mouseleave', Swal.resumeTimer)
	}
})




/*document.getElementById("basic").addEventListener("click", (e) => {
  Swal2.fire("Any fool can use a computer")
})
document.getElementById("footer").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "error",
	title: "Oops...",
	text: "Something went wrong!",
	footer: "<a href>Why do I have this issue?</a>",
  })
})
document.getElementById("title").addEventListener("click", (e) => {
  Swal2.fire("The Internet?", "That thing is still around?", "question")
})
document.getElementById("success").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "success",
	title: "Success",
  })
})
document.getElementById("error").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "error",
	title: "Error",
  })
})
document.getElementById("warning").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "warning",
	title: "Warning",
  })
})
document.getElementById("info").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "info",
	title: "Info",
  })
})
document.getElementById("question").addEventListener("click", (e) => {
  Swal2.fire({
	icon: "question",
	title: "Question",
  })
})
document.getElementById("text").addEventListener("click", (e) => {
  Swal2.fire({
	title: "Enter your IP address",
	input: "text",
	inputLabel: "Your IP address",
	showCancelButton: true,
  })
})
document.getElementById("email").addEventListener("click", async (e) => {
  const { value: email } = await Swal2.fire({
	title: "Input email address",
	input: "email",
	inputLabel: "Your email address",
	inputPlaceholder: "Enter your email address",
  })

  if (email) {
	Swal2.fire(`Entered email: ${email}`)
  }
})
document.getElementById("url").addEventListener("click", async (e) => {
  const { value: url } = await Swal2.fire({
	input: "url",
	inputLabel: "URL address",
	inputPlaceholder: "Enter the URL",
  })

  if (url) {
	Swal2.fire(`Entered URL: ${url}`)
  }
})
document.getElementById("password").addEventListener("click", async (e) => {
  const { value: password } = await Swal2.fire({
	title: "Enter your password",
	input: "password",
	inputLabel: "Password",
	inputPlaceholder: "Enter your password",
	inputAttributes: {
	  maxlength: 10,
	  autocapitalize: "off",
	  autocorrect: "off",
	},
  })

  if (password) {
	Swal2.fire(`Entered password: ${password}`)
  }
})
document.getElementById("textarea").addEventListener("click", async (e) => {
  const { value: text } = await Swal2.fire({
	input: "textarea",
	inputLabel: "Message",
	inputPlaceholder: "Type your message here...",
	inputAttributes: {
	  "aria-label": "Type your message here",
	},
	showCancelButton: true,
  })

  if (text) {
	Swal2.fire(text)
  }
})
document.getElementById("select").addEventListener("click", async (e) => {
  const { value: fruit } = await Swal2.fire({
	title: "Select field validation",
	input: "select",
	inputOptions: {
	  Fruits: {
		apples: "Apples",
		bananas: "Bananas",
		grapes: "Grapes",
		oranges: "Oranges",
	  },
	  Vegetables: {
		potato: "Potato",
		broccoli: "Broccoli",
		carrot: "Carrot",
	  },
	  icecream: "Ice cream",
	},
	inputPlaceholder: "Select a fruit",
	showCancelButton: true,
	inputValidator: (value) => {
	  return new Promise((resolve) => {
		if (value === "oranges") {
		  resolve()
		} else {
		  resolve("You need to select oranges :)")
		}
	  })
	},
  })

  if (fruit) {
	Swal2.fire(`You selected: ${fruit}`)
  }
})*/

// Toasts
/*document.getElementById('toast-success').addEventListener('click', () => {
  Toast.fire({
	icon: 'success',
	title: 'Signed in successfully'
  })
})*/

if (document.getElementById('toast-success-updated-data')) {
	document.getElementById('toast-success-updated-data').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Updated successfully'
		})
	})
}
if (document.getElementById('toast-failed-updated_data')) {
	document.getElementById('toast-failed-updated_data').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Images error. Please try again'
		})
	})
}
if (document.getElementById('toast-failed-create_data')) {
	document.getElementById('toast-failed-create_data').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Input error. Please try again'
		})
	})
}

if (document.getElementById('toast-success-created-data')) {
	document.getElementById('toast-success-created-data').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Created successfully'
		})
	})
}
if (document.getElementById('toast-failed-register-car')) {
	document.getElementById('toast-failed-register-car').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Input error. Please try again'
		})
	})
}
if (document.getElementById('toast-success-registered-car')) {
	document.getElementById('toast-success-registered-car').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Registered successfully'
		})
	})
}
// Điệp
document.getElementById("logout").addEventListener("click", async () => {
	const { isConfirmed } = await Swal2.fire({
		title: "Confirm Logout",
		text: "Are you sure you want to logout?",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "Yes, logout",
		cancelButtonText: "Cancel",
	});

	if (isConfirmed) {

		checkSubmitLogout();
	} else {
		Swal2.fire("Logout cancelled", "", "info");
	}
});
/*if (document.getElementById("toast-success-edit-user")) {
	document.getElementById("toast-success-edit-user").addEventListener("click", () => {
		Swal2.fire({
			icon: "success",
			title: "Success",
		})
	})
}
if (document.getElementById("toast-error-edit-user")) {
	document.getElementById("toast-error-edit-user").addEventListener("click", () => {
		Swal2.fire({
			icon: "error",
			title: "Error",
		})
	})
}*/
// End Điệp
/*document.getElementById('toast-warning').addEventListener('click', () => {
  Toast.fire({
	icon: 'warning',
	title: 'Please input your email'
  })
})
document.getElementById('toast-failed').addEventListener('click', () => {
  Toast.fire({
	icon: 'error',
	title: 'Transaction error. Please try again later'
  })
})*/