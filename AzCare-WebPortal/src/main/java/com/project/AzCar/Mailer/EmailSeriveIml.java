package com.project.AzCar.Mailer;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

@Service("EmailService")
public class EmailSeriveIml implements EmailService {

	private static final String NOREPLY_ADDRESS = "azCar@shop.com";

	@Autowired
	private JavaMailSender emailSender;


	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	@Value("classpath:/mail-logo.png")
	private Resource resourceFile;


	@Override
	public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)
			throws MessagingException {

		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);

		String htmlBody = thymeleafTemplateEngine.process("emails/template-newuser.html", thymeleafContext);

		try {
			sendHtmlMessage(to, subject, htmlBody);
		} catch (MessagingException | jakarta.mail.MessagingException e) {
			e.printStackTrace();
		}
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody)
			throws MessagingException, jakarta.mail.MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(NOREPLY_ADDRESS);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		helper.addInline("attachment.png", resourceFile);
		emailSender.send(message);
	}
	
	
	

}
