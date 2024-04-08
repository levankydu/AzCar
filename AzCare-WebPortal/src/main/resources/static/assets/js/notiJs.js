var stompClient = null;
	var privateStompClient = null;

	var socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log(frame);
		stompClient.subscribe('/all/messages', function(result) {
			show(JSON.parse(result.body));
		});
	});

	var privateSocket = new SockJS('/ws/private');
	privateStompClient = Stomp.over(privateSocket);
	privateStompClient.connect({}, function(frame) {
		console.log(frame);
		privateStompClient.subscribe('/user/specific', function(result) {
			console.log(result.body)
			show(JSON.parse(result.body));
		});
	});

	function sendMessage() {
		var text = document.getElementById('text').value;
		stompClient.send("/app/application", {}, JSON.stringify({
			'text' : text
		}));
	}

	function sendPrivateMessage() {
		var text = document.getElementById('privateText').value;
		var to = document.getElementById('to').value;
		stompClient.send("/app/private", {}, JSON.stringify({
			'text' : text,
			'to' : to
		}));
	}

	function show(message) {
		var response = document.getElementById('messages');
		var p = document.createElement('p');
		p.innerHTML = "From Admin: " + message.text;
		response.appendChild(p);
		countParagraphs();

	}

	function countParagraphs() {
		var messagesDiv = document.getElementById('messages');
		var paragraphs = messagesDiv.getElementsByTagName('p');
		var paragraphCount = paragraphs.length;
		var spanElement = document.getElementById('notiCounter');
		var noti = document.getElementById('noti');
		var shell = document.getElementById('shell');
		var ball = document.getElementById('ball');
		if (spanElement) {
			spanElement.textContent = paragraphCount;

			noti.classList.add('bell-icon');
			shell.classList.add('bell-icon__shell');
			ball.classList.add('bell-icon__ball');
			spanElement.textContent = paragraphCount;
			spanElement.style.backgroundColor = '#f72918';
			spanElement.style.borderRadius = '50%';
			spanElement.style.right = '24%';
			spanElement.style.width = '25px';
			spanElement.style.height = '25px';
			spanElement.style.top = '20%';
			spanElement.style.display = 'flex';
			spanElement.style.justifyContent = 'center';
			spanElement.style.position = 'absolute';
			spanElement.style.fontFamily = '"Copse", serif';
			spanElement.style.fontSize = '14px';
			var noti = document.getElementById('openNoti');
			if (noti) {
				noti.addEventListener('click', function(event) {
					event.preventDefault();
					console.log("trigger click")
					var buttonId = "six";
					document.getElementById('modal-container').removeAttribute(
							'class');
					document.getElementById('modal-container').classList
							.add(buttonId);
					document.body.classList.add('modal-active');
				});

				document.getElementById('modal-container').addEventListener(
						'click', function(event) {
							event.stopPropagation();
							this.classList.add('out');
							document.body.classList.remove('modal-active');
						});
			}
		}
	}