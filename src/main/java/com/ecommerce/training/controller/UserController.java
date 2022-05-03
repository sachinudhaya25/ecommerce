package com.ecommerce.training.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.UserDto;
import com.ecommerce.training.exception.InvalidInputException;
import com.ecommerce.training.exception.Unauthorized;
import com.ecommerce.training.models.User;
import com.ecommerce.training.service.ProductServiceImpl;
import com.ecommerce.training.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveUser(@Valid  @RequestHeader("Authorization") @RequestBody User user)
	{
		logger.info("Save user method start....");
		return userService.saveUser(user); 
	}
	@GetMapping("/get")
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "Authorization", value = "Bearer token", 
//	                      required = true, dataType = "string", paramType = "header") })
	public List<UserDto> getAllUsers()
	{ 
		logger.info("Get all users method start....");
			return userService.getAllUsers();
	
	}
	@GetMapping("/get/{id}")
	public GenericResponse getUserById(@PathVariable Long id) throws InvalidInputException
	{
		if(id==0)
		{
		    throw new InvalidInputException();	
		}
		logger.info("Get user by id method start....");
		return userService.getUserById(id);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@RequestBody UserDto userDto,@PathVariable Long id)
	{
		logger.info("Update user method start....");
		return userService.updateUser(userDto, id);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id)
	{
		logger.info("Delete user method start....");
		return userService.deleteUser(id);
	}
	
}
