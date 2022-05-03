package com.ecommerce.training.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.UserDto;
import com.ecommerce.training.exception.ResourceNotFoundException;

import com.ecommerce.training.models.User;
import com.ecommerce.training.payload.response.JwtResponse;
import com.ecommerce.training.payload.response.MessageResponse;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.utility.AppConstant;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	PasswordEncoder encoder;
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	UserRepository userRepository;
	@Override
	public ResponseEntity<?> saveUser(User user) {
		user.setStatus(2);
		user.setPassword(encoder.encode(user.getPassword()));
		User u1=userRepository.save(user);
		logger.info("Save user operation is performed in User Management");
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
 
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> user=userRepository.findAllByStatus(AppConstant.ActiveStatus);
		List<UserDto> userDto=new ArrayList<UserDto>(user.size());
		for ( User u : user ) {
			userDto.add( addUsertoUserDto( u ) );
        }
		logger.info("Get All Users operation is performed in User Management");
		return userDto;
	}

	@Override
	
	public GenericResponse getUserById(Long id) {
		User user=userRepository.findByIdAndStatus(id, AppConstant.ActiveStatus);
		if(user==null)
		{
			throw new ResourceNotFoundException("User is not exist by this id:"+id);
		}
		logger.info("Get user by id operation is performed in User Management");
		GenericResponse genericResponse=new GenericResponse();
		genericResponse.setData(user);
		genericResponse.setDescription("Product Management");
		genericResponse.setMessage("Products");
		genericResponse.setStatus("Active");
		return genericResponse;
		
	}



	@Override
	public ResponseEntity<?> updateUser(UserDto userDto, Long id) {
		User user=userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with this id :" + id));
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setEmpid(userDto.getUsername().substring(0, 3).toUpperCase());
		user.setPassword(encoder.encode(userDto.getPassword()));
		User updatedUser = userRepository.save(user);
		UserDto userDt=new UserDto();
		userDt.setId(updatedUser.getId());
		userDt.setEmail(updatedUser.getEmail());
		userDt.setMobile(updatedUser.getMobile());
		userDt.setUsername(updatedUser.getUsername());
		//userDt.setEmpid(updatedUser.getEmpid());
		logger.info("Update user operation is performed in User Management");
		return ResponseEntity.ok(new MessageResponse("User Updated successfully!"));
	}

	@Override
	public ResponseEntity<?> deleteUser(Long id) {
		User user=userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with this id :" + id));
		user.setStatus(AppConstant.InactiveStatus);
		User updateUser=userRepository.save(user);
		logger.info("Delete user operation is performed in User Management");
		return ResponseEntity.ok(new MessageResponse("User Deleted successfully!"));
	}

	@Override
	public UserDto addUsertoUserDto(User user) {
		UserDto userDto=new UserDto();
		userDto.setId(user.getId());
		userDto.setMobile(user.getMobile());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		return userDto;
	}

	

}
