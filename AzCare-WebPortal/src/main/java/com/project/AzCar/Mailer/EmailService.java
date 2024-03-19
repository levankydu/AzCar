package com.project.AzCar.Mailer;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

public interface EmailService {
	void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)
			throws IOException, MessagingException;
	
}
