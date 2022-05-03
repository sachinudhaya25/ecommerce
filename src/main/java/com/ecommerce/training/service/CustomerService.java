package com.ecommerce.training.service;

import java.util.List;


import org.springframework.http.ResponseEntity;

import com.ecommerce.training.models.Customer;




public interface CustomerService {

	    
	
	    Customer saveCustomers(Customer customer);
		List<Customer> getAllCustomers();
		Customer getCustomerById(Long id);
		ResponseEntity<Customer> updateCustomer(Customer customer,Long id);
		ResponseEntity<?>  deleteCustomer(Long id);
		
}
