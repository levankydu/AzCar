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

if (document.getElementById("loadding-sendEmail")) {
	document.getElementById("loadding-sendEmail").addEventListener("click", (e) => {
		Swal2.fire({
			icon: "success",
			html: `<p> Sending mail... </p>`,
			timerProgressBar: true,
			didOpen: () => {
				Swal.showLoading();
			},
			willClose: () => {
				clearInterval(timerInterval);
			},
			showCancelButton: false,
			showConfirmButton: false,
			allowOutsideClick: false,
			allowEscapeKey: false
		})
	})
}

if (document.getElementById("loadding-addLicense")) {
	document.getElementById("loadding-addLicense").addEventListener("click", (e) => {
		Swal2.fire({
			icon: "question",
			timer: 8000,
			html: `<p> Checking ... </p>`,
			timerProgressBar: true,
			didOpen: () => {
				Swal.showLoading();
			},
			willClose: () => {
				clearInterval(timerInterval);
			},
			showCancelButton: false,
			showConfirmButton: false,
			allowOutsideClick: false,
			allowEscapeKey: false
		});

	}
	);
}
if (document.getElementById("addLicense-false")) {
	document.getElementById("addLicense-false").addEventListener("click", (e) => {
		Swal.fire({
			icon: "error",
			title: "Oops...",
			text: "Your Driver License in not valid",
		});

	}
	);
}if (document.getElementById("addLicense-success")) {
	document.getElementById("addLicense-success").addEventListener("click", (e) => {
		Swal.fire({
			icon: "success",
			title: "Vaid Driver License",
			text: "Your Driver License validated",
		});

	}
	);
}
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

if (document.getElementById("accept-tuReview")) {
	document.getElementById("accept-tuReview").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter ''verify'' in lowercase",
			showCancelButton: true,

		}).then((result) => {

			if (result.value && result.value.toLowerCase() === 'verify') {


				$('#verify').submit();
				console.log("User entered 'verify'");
			} else {

				console.log("User did not enter 'verify' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}


if (document.getElementById("decline-tuReview")) {
	document.getElementById("decline-tuReview").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter ''decline'' in lowercase",
			showCancelButton: true,

		}).then((result) => {
			if (result.value && result.value.toLowerCase() === 'decline') {

				$('#declined').submit();
				console.log("User entered 'verify'");
			} else {

				console.log("User did not enter 'decline' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}
if (document.getElementById("accept-plates")) {
	document.getElementById("accept-plates").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter ''verify'' in lowercase",
			showCancelButton: true,

		}).then((result) => {

			if (result.value && result.value.toLowerCase() === 'verify') {
				$('#privateText').val("''Your License Plate is verified for booking, check your email for full information''");
				sendPrivateMessage();
				$('#verify-pl').submit();
				$('#loadding-sendEmail').click();
			} else {

				console.log("User did not enter 'verify' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}


if (document.getElementById("decline-plates")) {
	document.getElementById("decline-plates").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter reason in lowercase",
			showCancelButton: true,

		}).then((result) => {
			if (result.value && result.value.toLowerCase() != '') {
				$('#privateText').val("''Your License Plate is declined for booking now, check your email for full information''");
				reason.value = result.value.toLowerCase();
				sendPrivateMessage();
				$('#declined-pl').submit();
				$('#loadding-sendEmail').click();
			} else {
				console.log("User did not enter 'decline' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}

if (document.getElementById("accept")) {
	document.getElementById("accept").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter ''verify'' in lowercase",
			showCancelButton: true,

		}).then((result) => {

			if (result.value && result.value.toLowerCase() === 'verify') {
				privateText.value = "''Your car is available for rent now, check your email for full information''";
				sendPrivateMessage();
				verify.submit();
				$('#loadding-sendEmail').click();
			} else {

				console.log("User did not enter 'verify' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}


if (document.getElementById("decline")) {
	document.getElementById("decline").addEventListener("click", (e) => {
		e.preventDefault();
		Swal2.fire({
			title: "Confirm verify",
			input: "text",
			inputLabel: "Enter reason in lowercase",
			showCancelButton: true,

		}).then((result) => {
			if (result.value && result.value.toLowerCase() != '') {
				privateText.value = "''Your car is declined for rent now, check your email for full information''";
				reason.value = result.value.toLowerCase();
				sendPrivateMessage();
				declined.submit();
				$('#loadding-sendEmail').click();
			} else {

				console.log("User did not enter 'decline' or canceled");
				Swal2.fire({
					icon: "error",
					title: "Oops...",
					text: "Something went wrong!",

				})
			}
		});
	});
}


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
if (document.getElementById('toast-failed-updated-car')) {
	document.getElementById('toast-failed-updated-car').addEventListener('click', () => {
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
if (document.getElementById('toast-failed-login')) {
	document.getElementById('toast-failed-login').addEventListener('click', () => {
		Toast.fire({
			icon: 'warning',
			title: 'You need login to continue'
		})
	})
}


// Điệp
if (document.getElementById("logout")) {
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
}
if (document.getElementById("toast-success-logout")) {
	document.getElementById("toast-success-logout").addEventListener("click", () => {

		Swal2.fire({
			icon: "success",
			title: "Success",
		})
	})
}

if (document.getElementById('toast-success-registered-user')) {
	document.getElementById('toast-success-registered-user').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Registered successfully'
		})
	})
}
if (document.getElementById('toast-failed-register-user')) {
	document.getElementById('toast-failed-register-user').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Registeres failed'
		})
	})
}
if (document.getElementById('toast-failed-register-exit')) {
	document.getElementById('toast-failed-register-exit').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Email eixt'
		})
	})
}
if (document.getElementById('toast-success-edit-user')) {
	document.getElementById('toast-success-edit-user').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Edit successfully'
		})
	})
}
if (document.getElementById('toast-failed-edit-user')) {
	document.getElementById('toast-failed-edit-user').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Edit failed'
		})
	})
}
if (document.getElementById('toast-success-change-password')) {
	document.getElementById('toast-success-change-password').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Change Password successfully'
		})
	})
}
if (document.getElementById('toast-failed-change-password')) {
	document.getElementById('toast-failed-change-password').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Change Password failed'
		})
	})
}
if (document.getElementById('toast-success-upload-avatar')) {
	document.getElementById('toast-success-upload-avatar').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Upload successfully'
		})
	})
}
if (document.getElementById('toast-failed-upload-avatar')) {
	document.getElementById('toast-failed-upload-avatar').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Upload failed'
		})
	})
}

if (document.getElementById('toast-success-loginGoogle')) {
	document.getElementById('toast-success-loginGoogle').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Rgister Google successfully'
		})
	})
}
if (document.getElementById('toast-failed-loginGoogle')) {
	document.getElementById('toast-failed-loginGoogle').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Rgister Google failed'
		})
	})
}

if (document.getElementById('toast-success-reset-password')) {
	document.getElementById('toast-success-reset-password').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Reset Password successfully'
		})
	})
}
if (document.getElementById('toast-failed-reset-password')) {
	document.getElementById('toast-failed-reset-password').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Reset Password failed'
		})
	})
}
// End Điệp

if (document.getElementById('toast-failed-uploadDriveLicense')) {
	document.getElementById('toast-failed-uploadDriveLicense').addEventListener('click', () => {
		Toast.fire({
			icon: 'error',
			title: 'Input error. Please try again'
		})
	})
}
if (document.getElementById('toast-success-uploadDriveLicense')) {
	document.getElementById('toast-success-uploadDriveLicense').addEventListener('click', () => {
		Toast.fire({
			icon: 'success',
			title: 'Upload successfully'
		})
	})
}

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