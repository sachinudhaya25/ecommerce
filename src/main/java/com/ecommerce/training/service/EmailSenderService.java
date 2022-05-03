package com.ecommerce.training.service;

import javax.mail.MessagingException;

public interface EmailSenderService {

	public void sendSimpleEmail(String toEmail,String body,String subject);
	public void sendEmailWithAttachment(String toEmail,String body,String subject,String attachment) throws MessagingException;
	public void sendHtmlMessage(String toEmail,String body,String subject) throws MessagingException;
}
