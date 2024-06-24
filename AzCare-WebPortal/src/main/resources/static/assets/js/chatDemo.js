$(function() {
	var INDEX = 0;

	$("#chat-submit").click(function(e) {
		e.preventDefault();
		var msg = $("#chat-input").val();
		if (msg.trim() == '') {
			return false;
		}
		sendPrivateMessageAdmin();
		generate_message(msg, 'self',);


		var buttons = [
			{
				name: 'Existing User',
				value: 'existing'
			},
			{
				name: 'New User',
				value: 'new'
			}
		];
		/*setTimeout(function() {      
		  generate_messageAdmin(msg, 'user');  
		}, 1000)*/

	});
	$("#chat-Adminsubmit").click(function(e) {
		e.preventDefault();
		var msg = $("#chat-input").val();
		if (msg.trim() == '') {
			return false;
		}
		var messagesDiv = document.getElementById("messages");
		var dataTo = findReplyingMessage();
		var inputElement = document.getElementById('chat-input');
		console.log("dataTo" + dataTo);
		if (dataTo != null) {
			var parts = dataTo.split(":");
			var part0 = parts[0].trim();
			var final = part0.split("#");
			var email = final[1].trim();
			ReplyPrivateMessageAdmin(email);
			generate_messageAdmin(msg+", #"+email, 'user');
			inputElement.value = "";
		}
		else {
			var inputs = messagesDiv.getElementsByTagName("input");
			var ids = [];
			for (var i = 0; i < inputs.length; i++) {
				ids.push(inputs[i].id);
			}
			console.log(ids);
			if (inputs.length > 0) {
				var lastInputId = inputs[inputs.length - 1].id;
			}
			ReplyPrivateMessageAdmin(lastInputId);
			generate_messageAdmin(msg+", #"+lastInputId, 'user');
			inputElement.value = "";
		}



		var buttons = [
			{
				name: 'Existing User',
				value: 'existing'
			},
			{
				name: 'New User',
				value: 'new'
			}
		];
		/*setTimeout(function() {      
		  generate_messageAdmin(msg, 'user');  
		}, 1000)*/

	});
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
	};

	function generate_messageAdmin(msg, type) {
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
	};
	function generate_button_message(msg, buttons) {
		/* Buttons should be object array 
		  [
			{
			  name: 'Existing User',
			  value: 'existing'
			},
			{
			  name: 'New User',
			  value: 'new'
			}
		  ]
		*/
		INDEX++;
		var btn_obj = buttons.map(function(button) {
			return "              <li class=\"button\"><a href=\"javascript:;\" class=\"btn btn-primary chat-btn\" chat-value=\"" + button.value + "\">" + button.name + "<\/a><\/li>";
		}).join('');
		var str = "";
		str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg user\">";
		str += "          <span class=\"msg-avatar\">";
		str += "            <img src=\"https://png.pngtree.com/png-clipart/20230409/original/pngtree-admin-and-customer-service-job-vacancies-png-image_9041264.png\">";
		str += "          <\/span>";
		str += "          <div class=\"cm-msg-text\">";
		str += msg;
		str += "          <\/div>";
		str += "          <div class=\"cm-msg-button\">";
		str += "            <ul>";
		str += btn_obj;
		str += "            <\/ul>";
		str += "          <\/div>";
		str += "        <\/div>";
		$(".chat-logs").append(str);
		$("#cm-msg-" + INDEX).hide().fadeIn(300);
		$(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
		$("#chat-input").attr("disabled", true);
	}

	$(document).delegate(".chat-btn", "click", function() {
		var value = $(this).attr("chat-value");
		var name = $(this).html();
		$("#chat-input").attr("disabled", false);
		generate_message(name, 'self');
	});

	$("#chat-circle").click(function() {
		$("#chat-circle").toggle('scale');
		$(".chat-box").toggle('scale');
	});

	$(".chat-box-toggle").click(function() {
		$("#chat-circle").toggle('scale');
		$(".chat-box").toggle('scale');
	});

});



function generate_messageUser(data, type) {
	var INDEX = $(".chat-msg").length + 1; // Số thứ tự mới cho tin nhắn
	var str = "";
	str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
	str += "          <span class=\"msg-avatar\">";
	str += "            <img src=\"https://cdn-icons-png.flaticon.com/512/3177/3177440.png\">";
	str += "          </span>";
	str += "          <div class=\"cm-msg-text\">";
	str += data;
	str += "          </div>";
	str += "<button class=\"reply-btn\" id=\"replyMessage-" + INDEX + "\" onclick=\"replyMessage('" + data + "', " + INDEX + ")\">Reply</button>";

	str += "        </div>";

	$(".chat-logs").append(str);
	var $newMsg = $("#cm-msg-" + INDEX); // Lấy phần tử mới vừa thêm vào
	$newMsg.find('.reply-btn').css({
		'position': 'relative',
		'top': '50%',
		'right': '10px',
		'transform': 'translateY(-50%)',
		'background-color': '#007bff',
		'color': '#fff',
		'padding': '5px 10px',
		'border': 'none',
		'border-radius': '5px',
		'cursor': 'pointer',
		'z-index': '1'
	});
	$newMsg.find('.cm-msg-text').css('padding-right', '80px'); // Đảm bảo có khoảng trống cho nút Reply

	$newMsg.hide().fadeIn(300);
	if (type == 'self') {
		$("#chat-input").val('');
	}
	$(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
};

function replyMessage(messageContent, index) {
	var parts = messageContent.split(":");
	var part0 = parts[0].trim();
	var final = part0.split("#");
	var email = final[1].trim();
	var inputElement = document.getElementById('chat-input');
	var replyButton = document.getElementById('replyMessage-' + index);
	if (inputElement && replyButton) {
		if (replyButton.innerText === "Reply") {
			replyButton.textContent = "Replying";
			inputElement.placeholder = "Replying to: " + email;
			var messages = document.getElementsByClassName("chat-msg");
			for (var i = 0; i < messages.length; i++) {
				var message = messages[i];
				var otherReplyButton = message.getElementsByClassName("reply-btn")[0];
				if (otherReplyButton && otherReplyButton !== replyButton && otherReplyButton.innerText === "Replying") {
					otherReplyButton.textContent = "Reply";
				}
			}
		} else {
			replyButton.textContent = "Reply";
			inputElement.placeholder = "Enter message...";
		}
	}
}
function findReplyingMessage() {
	var messages = document.getElementsByClassName("chat-msg");
	for (var i = 0; i < messages.length; i++) {
		var message = messages[i];
		var replyButton = message.getElementsByClassName("reply-btn")[0];
		if (replyButton && replyButton.innerText === "Replying") {
			return message.querySelector(".cm-msg-text").textContent;
		}
	}
	return null;
}

