package com.ecommerce.training.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDto {

	private Long id;
	private String username;
	private String email;
	private Long mobile;
	@JsonIgnore
	private String password;
	
	
	public UserDto(Long id, String username, String email, Long mobile) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.mobile = mobile;
	}
	public UserDto() {
		// TODO Auto-generated constructor stub
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
