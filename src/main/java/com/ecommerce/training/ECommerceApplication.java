package com.ecommerce.training;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import com.ecommerce.training.service.EmailSenderService;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ECommerceApplication {
	
    @Autowired
	private EmailSenderService emailService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}
	@Bean
    public Docket swaggerConfiguration() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .select()                                  
	          .apis(RequestHandlerSelectors.basePackage("com.ecommerce"))              
	          .paths(PathSelectors.any())                          
	          .build();                                           
	 }
	
	@EventListener(ApplicationReadyEvent.class)
	public void triggerMail() throws MessagingException {
//		emailService.sendSimpleEmail("@gmail.com", "Mail body", "Mail subject");
//		emailService.sendEmailWithAttachment("@gmail.com", "Mail body", "Mail subject", "D:\\private\\sachin\\mail.txt");
		//emailService.sendHtmlMessage("@gmail.com", "Mail body", "Mail subject");
	}
	

}
