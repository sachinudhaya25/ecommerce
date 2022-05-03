package com.ecommerce.training.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	@Override
	public void sendSimpleEmail(String toEmail, String body, String subject) {
		// TODO Auto-generated method stub
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("uthayaraj99@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		//mailSender.send(message);
		System.out.println("mail send....");
	}

	@Override
	public void sendEmailWithAttachment(String toEmail, String body, String subject,String attachment) throws MessagingException {
		// TODO Auto-generated method stub
		MimeMessage mimeMessage=mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
		mimeMessageHelper.setFrom("uthayaraj99@gmail.com");
		mimeMessageHelper.setTo(toEmail);
		mimeMessageHelper.setText(body);
		mimeMessageHelper.setSubject(subject);
//		mimeMessageHelper.setBcc("uthayaraj99@gmail.com");
//		mimeMessageHelper.setCc("uthayaraj99@gmail.com");
		
		FileSystemResource fileSystem=new FileSystemResource(new File(attachment));
		mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
		//mailSender.send(mimeMessage);
	}

	@Override
	public void sendHtmlMessage(String toEmail, String body, String subject) throws MessagingException {
		
		 MimeMessage message = mailSender.createMimeMessage();
	     MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
	     Context context = new Context();
	     Map<String,Object> data=new HashMap<>();
	     data.put("name", subject);
	     context.setVariables(data);
	     helper.setFrom("uthayaraj99@gmail.com");
	     helper.setTo(toEmail);
	     helper.setSubject(subject);
	     String html = templateEngine.process("welcome-email.html", context);
	     helper.setText(html, true);
	     mailSender.send(message);
	}

}
