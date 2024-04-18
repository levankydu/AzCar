$(function() {
	var INDEX = 0;
	$("#chat-submit").click(function(e) {
		e.preventDefault();
		var msg = $("#chat-input").val();
		if (msg.trim() == '') {
			return false;
		}
		sendPrivateMessageAdmin();
		generate_message(msg, 'self');
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
		var inputs = messagesDiv.getElementsByTagName("input");
		var ids = [];
		for (var i = 0; i < inputs.length; i++) {
			ids.push(inputs[i].id);
		}
		if (inputs.length > 0) {
			var lastInputId = inputs[inputs.length - 1].id;
		}
		ReplyPrivateMessageAdmin(lastInputId);
		generate_messageAdmin(msg, 'user');
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
	})

	$("#chat-circle").click(function() {
		$("#chat-circle").toggle('scale');
		$(".chat-box").toggle('scale');
	})

	$(".chat-box-toggle").click(function() {
		$("#chat-circle").toggle('scale');
		$(".chat-box").toggle('scale');
	})

})