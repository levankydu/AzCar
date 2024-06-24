var __assign = (this && this.__assign) || function() {
	__assign = Object.assign || function(t) {
		for (var s, i = 1, n = arguments.length; i < n; i++) {
			s = arguments[i];
			for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
				t[p] = s[p];
		}
		return t;
	};
	return __assign.apply(this, arguments);
};
// playground: stackblitz.com/edit/countup-typescript
var CountUp = /** @class */ (function() {
	function CountUp(target, endVal, options) {
		var _this = this;
		this.endVal = endVal;
		this.options = options;
		this.version = '2.8.0';
		this.defaults = {
			startVal: 0,
			decimalPlaces: 0,
			duration: 2,
			useEasing: true,
			useGrouping: true,
			useIndianSeparators: false,
			smartEasingThreshold: 999,
			smartEasingAmount: 333,
			separator: ',',
			decimal: '.',
			prefix: '$ ',
			suffix: '',
			enableScrollSpy: false,
			scrollSpyDelay: 200,
			scrollSpyOnce: false,
		};
		this.finalEndVal = null; // for smart easing
		this.useEasing = true;
		this.countDown = false;
		this.error = '';
		this.startVal = 0;
		this.paused = true;
		this.once = false;
		this.count = function(timestamp) {
			if (!_this.startTime) {
				_this.startTime = timestamp;
			}
			var progress = timestamp - _this.startTime;
			_this.remaining = _this.duration - progress;
			// to ease or not to ease
			if (_this.useEasing) {
				if (_this.countDown) {
					_this.frameVal = _this.startVal - _this.easingFn(progress, 0, _this.startVal - _this.endVal, _this.duration);
				}
				else {
					_this.frameVal = _this.easingFn(progress, _this.startVal, _this.endVal - _this.startVal, _this.duration);
				}
			}
			else {
				_this.frameVal = _this.startVal + (_this.endVal - _this.startVal) * (progress / _this.duration);
			}
			// don't go past endVal since progress can exceed duration in the last frame
			var wentPast = _this.countDown ? _this.frameVal < _this.endVal : _this.frameVal > _this.endVal;
			_this.frameVal = wentPast ? _this.endVal : _this.frameVal;
			// decimal
			_this.frameVal = Number(_this.frameVal.toFixed(_this.options.decimalPlaces));
			// format and print value
			_this.printValue(_this.frameVal);
			// whether to continue
			if (progress < _this.duration) {
				_this.rAF = requestAnimationFrame(_this.count);
			}
			else if (_this.finalEndVal !== null) {
				// smart easing
				_this.update(_this.finalEndVal);
			}
			else {
				if (_this.options.onCompleteCallback) {
					_this.options.onCompleteCallback();
				}
			}
		};
		// default format and easing functions
		this.formatNumber = function(num) {
			var neg = (num < 0) ? '-' : '';
			var result, x1, x2, x3;
			result = Math.abs(num).toFixed(_this.options.decimalPlaces);
			result += '';
			var x = result.split('.');
			x1 = x[0];
			x2 = x.length > 1 ? _this.options.decimal + x[1] : '';
			if (_this.options.useGrouping) {
				x3 = '';
				var factor = 3, j = 0;
				for (var i = 0, len = x1.length; i < len; ++i) {
					if (_this.options.useIndianSeparators && i === 4) {
						factor = 2;
						j = 1;
					}
					if (i !== 0 && (j % factor) === 0) {
						x3 = _this.options.separator + x3;
					}
					j++;
					x3 = x1[len - i - 1] + x3;
				}
				x1 = x3;
			}
			// optional numeral substitution
			if (_this.options.numerals && _this.options.numerals.length) {
				x1 = x1.replace(/[0-9]/g, function(w) { return _this.options.numerals[+w]; });
				x2 = x2.replace(/[0-9]/g, function(w) { return _this.options.numerals[+w]; });
			}
			return neg + _this.options.prefix + x1 + x2 + _this.options.suffix;
		};
		// t: current time, b: beginning value, c: change in value, d: duration
		this.easeOutExpo = function(t, b, c, d) {
			return c * (-Math.pow(2, -10 * t / d) + 1) * 1024 / 1023 + b;
		};
		this.options = __assign(__assign({}, this.defaults), options);
		this.formattingFn = (this.options.formattingFn) ?
			this.options.formattingFn : this.formatNumber;
		this.easingFn = (this.options.easingFn) ?
			this.options.easingFn : this.easeOutExpo;
		this.startVal = this.validateValue(this.options.startVal);
		this.frameVal = this.startVal;
		this.endVal = this.validateValue(endVal);
		this.options.decimalPlaces = Math.max(0 || this.options.decimalPlaces);
		this.resetDuration();
		this.options.separator = String(this.options.separator);
		this.useEasing = this.options.useEasing;
		if (this.options.separator === '') {
			this.options.useGrouping = false;
		}
		this.el = (typeof target === 'string') ? document.getElementById(target) : target;
		if (this.el) {
			this.printValue(this.startVal);
		}
		else {
			this.error = '[CountUp] target is null or undefined';
		}
		// scroll spy
		if (typeof window !== 'undefined' && this.options.enableScrollSpy) {
			if (!this.error) {
				// set up global array of onscroll functions to handle multiple instances
				window['onScrollFns'] = window['onScrollFns'] || [];
				window['onScrollFns'].push(function() { return _this.handleScroll(_this); });
				window.onscroll = function() {
					window['onScrollFns'].forEach(function(fn) { return fn(); });
				};
				this.handleScroll(this);
			}
			else {
				console.error(this.error, target);
			}
		}
	}
	CountUp.prototype.handleScroll = function(self) {
		if (!self || !window || self.once)
			return;
		var bottomOfScroll = window.innerHeight + window.scrollY;
		var rect = self.el.getBoundingClientRect();
		var topOfEl = rect.top + window.pageYOffset;
		var bottomOfEl = rect.top + rect.height + window.pageYOffset;
		if (bottomOfEl < bottomOfScroll && bottomOfEl > window.scrollY && self.paused) {
			// in view
			self.paused = false;
			setTimeout(function() { return self.start(); }, self.options.scrollSpyDelay);
			if (self.options.scrollSpyOnce)
				self.once = true;
		}
		else if ((window.scrollY > bottomOfEl || topOfEl > bottomOfScroll) &&
			!self.paused) {
			// out of view
			self.reset();
		}
	};
	/**
	 * Smart easing works by breaking the animation into 2 parts, the second part being the
	 * smartEasingAmount and first part being the total amount minus the smartEasingAmount. It works
	 * by disabling easing for the first part and enabling it on the second part. It is used if
	 * useEasing is true and the total animation amount exceeds the smartEasingThreshold.
	 */
	CountUp.prototype.determineDirectionAndSmartEasing = function() {
		var end = (this.finalEndVal) ? this.finalEndVal : this.endVal;
		this.countDown = (this.startVal > end);
		var animateAmount = end - this.startVal;
		if (Math.abs(animateAmount) > this.options.smartEasingThreshold && this.options.useEasing) {
			this.finalEndVal = end;
			var up = (this.countDown) ? 1 : -1;
			this.endVal = end + (up * this.options.smartEasingAmount);
			this.duration = this.duration / 2;
		}
		else {
			this.endVal = end;
			this.finalEndVal = null;
		}
		if (this.finalEndVal !== null) {
			// setting finalEndVal indicates smart easing
			this.useEasing = false;
		}
		else {
			this.useEasing = this.options.useEasing;
		}
	};
	// start animation
	CountUp.prototype.start = function(callback) {
		if (this.error) {
			return;
		}
		if (this.options.onStartCallback) {
			this.options.onStartCallback();
		}
		if (callback) {
			this.options.onCompleteCallback = callback;
		}
		if (this.duration > 0) {
			this.determineDirectionAndSmartEasing();
			this.paused = false;
			this.rAF = requestAnimationFrame(this.count);
		}
		else {
			this.printValue(this.endVal);
		}
	};
	// pause/resume animation
	CountUp.prototype.pauseResume = function() {
		if (!this.paused) {
			cancelAnimationFrame(this.rAF);
		}
		else {
			this.startTime = null;
			this.duration = this.remaining;
			this.startVal = this.frameVal;
			this.determineDirectionAndSmartEasing();
			this.rAF = requestAnimationFrame(this.count);
		}
		this.paused = !this.paused;
	};
	// reset to startVal so animation can be run again
	CountUp.prototype.reset = function() {
		cancelAnimationFrame(this.rAF);
		this.paused = true;
		this.resetDuration();
		this.startVal = this.validateValue(this.options.startVal);
		this.frameVal = this.startVal;
		this.printValue(this.startVal);
	};
	// pass a new endVal and start animation
	CountUp.prototype.update = function(newEndVal) {
		cancelAnimationFrame(this.rAF);
		this.startTime = null;
		this.endVal = this.validateValue(newEndVal);
		if (this.endVal === this.frameVal) {
			return;
		}
		this.startVal = this.frameVal;
		if (this.finalEndVal == null) {
			this.resetDuration();
		}
		this.finalEndVal = null;
		this.determineDirectionAndSmartEasing();
		this.rAF = requestAnimationFrame(this.count);
	};
	CountUp.prototype.printValue = function(val) {
		var _a;
		if (!this.el)
			return;
		var result = this.formattingFn(val);
		if ((_a = this.options.plugin) === null || _a === void 0 ? void 0 : _a.render) {
			this.options.plugin.render(this.el, result);
			return;
		}
		if (this.el.tagName === 'INPUT') {
			var input = this.el;
			input.value = result;
		}
		else if (this.el.tagName === 'text' || this.el.tagName === 'tspan') {
			this.el.textContent = result;
		}
		else {
			this.el.innerHTML = result;
		}
	};
	CountUp.prototype.ensureNumber = function(n) {
		return (typeof n === 'number' && !isNaN(n));
	};
	CountUp.prototype.validateValue = function(value) {
		var newValue = Number(value);
		if (!this.ensureNumber(newValue)) {
			this.error = "[CountUp] invalid start or end value: ".concat(value);
			return null;
		}
		else {
			return newValue;
		}
	};
	CountUp.prototype.resetDuration = function() {
		this.startTime = null;
		this.duration = Number(this.options.duration) * 1000;
		this.remaining = this.duration;
	};
	return CountUp;
}());
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
		'text': text
	}));
}

function sendPrivateMessage() {
	var text = document.getElementById('privateText').value;
	var to = document.getElementById('to').value;


	stompClient.send("/app/private", {}, JSON.stringify({
		'text': text,
		'to': to,
	}));
}
function sendPrivateMessageAdmin() {
	var text = document.getElementById('chat-input').value;
	var to = document.getElementById('to').value;
	var contact = document.getElementById('contact').value;
	var from = document.getElementById('from').value;
	stompClient.send("/app/private", {}, JSON.stringify({
		'text': text,
		'to': to,
		'contact': contact,
		'from': from

	}));
}
function ReplyPrivateMessageAdmin(lastInputId) {
	var text = document.getElementById('chat-input').value;
	var to = lastInputId;
	var contact = document.getElementById('contact').value;
	var from = document.getElementById('from').value;
	stompClient.send("/app/private", {}, JSON.stringify({
		'text': text,
		'to': to,
		'contact': contact,
		'from': from

	}));
}

function sendPrivateMoney() {
	var text = document.getElementById('privateText').value;
	var to = document.getElementById('to').value;
	stompClient.send("/app/private", {}, JSON.stringify({
		'text': text,
		'to': to
	}));
}

function show(message) {
	var response = document.getElementById('messages');
	var adminRespones = document.getElementById('Adminmessages');
	var p = document.createElement('p');

	if (message.contact != "contact") {
		p.innerHTML = message.text;
		adminRespones.appendChild(p);
		countParagraphs('Adminmessages');
		let moneyStr = document.getElementById('profitResult').textContent;
		let moneyNumber = moneyStr.replace(/\$|,/g, '') + message.text;
		let demo = new CountUp('profitResult', eval(moneyNumber));
		var xhr = new XMLHttpRequest();
		xhr.open('GET', "/dashboard/chartDrawing", true);
		xhr.onload = function() {
			if (this.status >= 200 && this.status < 400) {
				// Xử lý dữ liệu trả về từ server
				var data = JSON.parse(this.response);
				console.log(data);
				Highcharts.chart('containerProfit', {

					chart: {
						type: 'column',
						styledMode: true
					},

					title: {
						text: '',
						align: 'left'
					},

					subtitle: {
						text: 'AzCar Profit data ',
						align: 'left'
					},

					xAxis: {
						categories: data.result.dayList
					},

					yAxis: [{ // Primary axis
						className: 'highcharts-color-0',
						title: {
							text: 'Income Value'
						}
					}, { // Secondary axis
						className: 'highcharts-color-1',
						opposite: true,
						title: {
							text: 'Expense Value'
						}
					}],

					plotOptions: {
						column: {
							borderRadius: 5
						}
					},

					series: [{
						name: 'Income Value',
						data: data.result.totalIn,
						tooltip: {
							valueSuffix: ' $'
						}
					}, {
						name: 'Expense Value',
						data: data.result.totalOut,
						yAxis: 0,
						tooltip: {
							valueSuffix: ' $'
						}
					}]
				});
			} else {
				console.error('Đã có lỗi từ server!');
			}
		};
		xhr.onerror = function() {
			console.error('Có lỗi khi thực hiện request!');
		};
		xhr.send();
		console.log(demo);
		if (!demo.error) {
			demo.start();
		} else {
			console.log('error');
		}
	}
	if (message.contact == "contact" && message.from != "admin@admin") {
		generate_messageUser("#"+message.from+": "+message.text, 'user');
/*		generate_message("#"+message.from+": "+message.text, 'user');
*/		
		
		/*
		p.innerHTML = "From Admin: " + message.text;
		response.appendChild(p);
		*/
		
		if (document.getElementById("chat-circle")) {
			var chatCircle = document.getElementById("chat-circle");
			if (!chatCircle.clicked) {
				chatCircle.click();
				chatCircle.clicked = true;
			}
		}
		if (!document.getElementById(message.from)) {

			var input = document.createElement("input");
			
			input.type = "text";
			input.value = message.from;
			input.id = message.from;
			input.id = message.from;
			response.appendChild(input);
		} else {
			console.log("Id" + message.from + " đã tồn tại!");
			var inputElement = document.getElementById(message.from);
            if (inputElement) {
                inputElement.remove();
            } else {
                console.log("Element not found");
            }
            var input = document.createElement("input");
			input.type = "text";
			input.value = message.from;
			input.id = message.from;
			input.id = message.from;
			response.appendChild(input);
            console.log("đã tạo mới ID :"+ message.from);
		}
	} if (message.from == "admin@admin") {
		generate_messageAdmin(message.text, 'user');
		
		if (document.getElementById("chat-circle")) {
			var chatCircle = document.getElementById("chat-circle");
			if (chatCircle.style.display === 'none' || chatCircle.style.display === '') {
				chatCircle.style.display = 'block';
			}

		}
	}
	var INDEX = 0;
	function generate_message(msg, type) {
		INDEX++;
		var str = "";
		str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
		str += "          <span class=\"msg-avatar\">";
		str += "            <img src=\"https://cdn-icons-png.flaticon.com/512/3177/3177440.png\">";
		str += "          <\/span>";
		str += "          <div class=\"cm-msg-text\">";
		str += msg;
		str += "          <\/div>";
		str += "        <\/div>";
		$(".chat-logs").append(str);
		$("#cm-msg-" + INDEX).hide().fadeIn(300);
		if (type == 'self') {
			$("#chat-input").val('');
		}
		$(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
	}
	function generate_messageAdmin(msg, type) {
		INDEX++;
		var str = "";
		str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
		str += "          <span class=\"msg-avatar\">";
		str += "            <img src=\"https://png.pngtree.com/png-clipart/20230409/original/pngtree-admin-and-customer-service-job-vacancies-png-image_9041264.png\">";
		str += "          <\/span>";
		str += "          <div class=\"cm-msg-text\">";
		str += msg;
		str += "          <\/div>";
		str += "        <\/div>";
		$(".chat-logs").append(str);
		$("#cm-msg-" + INDEX).hide().fadeIn(300);
		if (type == 'self') {
			$("#chat-input").val('');
		}
		$(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
	}

}
function showContact() {
	var response = document.getElementById('messagesContact');
	if (response) {

		p.innerHTML = "From Admin: " + message.text;
		response.appendChild(p);
		countParagraphs('messages');


	}
}

function countParagraphs(elementId) {

	var messagesDiv = document.getElementById(elementId);

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
