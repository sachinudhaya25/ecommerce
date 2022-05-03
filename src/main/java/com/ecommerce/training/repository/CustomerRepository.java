package com.ecommerce.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.training.models.Customer;




public interface CustomerRepository extends JpaRepository<Customer,Long> {
	List<Customer> findAllByStatus(int status);
	Customer findByIdAndStatus(Long id,int status);

}
