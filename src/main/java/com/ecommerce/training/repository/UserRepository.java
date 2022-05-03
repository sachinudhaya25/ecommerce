package com.ecommerce.training.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.ecommerce.training.dto.UserDto;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.models.User;




@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	User save(UserDto userDto);
	
	User findByIdAndStatus(Long id,int status);
	List<User> findAllByStatus(int status);
}
