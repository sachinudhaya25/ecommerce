package com.ecommerce.training.utility;

import java.util.Random;

import org.springframework.http.HttpStatus;


public class AppConstant {
	public static final int ActiveStatus=1; 
	public static final int InactiveStatus=0;
	
	
	
	//security
	public static final String jwtSecret="SecretKey";
	public static final String jwtExpirationMs="86400000";
	
	
	
	//product
	public static final String[] ProductHEADERs = { "Id", "Name", "Description", "Part Number","Picture","Gst","Price","Unit"};
	
	public static final int testStatus=HttpStatus.OK.value();
	
	public static final String getRandomNumbers() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 15) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
	
}
