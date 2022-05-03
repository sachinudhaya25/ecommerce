package com.ecommerce.training.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.UserDto;
import com.ecommerce.training.models.User;



public interface UserService {
	    ResponseEntity<?> saveUser(User user);
		List<UserDto> getAllUsers();
		GenericResponse getUserById(Long id);
		ResponseEntity<?> updateUser(UserDto userDto,Long id);
		ResponseEntity<?> deleteUser(Long id);
		UserDto addUsertoUserDto(User user);
}
