package com.ecommerce.training.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.training.exception.ResourceNotFoundException;
import com.ecommerce.training.models.Customer;
import com.ecommerce.training.payload.response.MessageResponse;
import com.ecommerce.training.repository.CustomerRepository;
import com.ecommerce.training.utility.AppConstant;






@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	CustomerRepository customerRepository;
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	@Override
	public List<Customer> getAllCustomers() {
		logger.info("Get all customer operation is performed in Customer Management");
		return customerRepository.findAllByStatus(AppConstant.ActiveStatus);
	}
	@Override
	public Customer saveCustomers(Customer customer) {
		customer.setStatus(AppConstant.ActiveStatus);
		logger.info("Save customer operation is performed in Customer Management");
		return customerRepository.save(customer);
	}
	@Override
	public Customer getCustomerById(Long id) {
		Customer cust=customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not exist with this id :" + id));
		logger.info("Get customer by id operation is performed in Customer Management");
		return customerRepository.findByIdAndStatus(id, AppConstant.ActiveStatus);
	}
	@Override
	public ResponseEntity<Customer> updateCustomer(Customer customer, Long id) {
		Customer cust=customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not exist with this id :" + id));
		
		Customer updatedCustomer = customerRepository.save(customer);
		logger.info("Update customer operation is performed in Customer Management");
		return ResponseEntity.ok(updatedCustomer);
	}
	@Override
	public ResponseEntity<?> deleteCustomer(Long id) {
		Customer customer=customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not exist with this id :" + id));
		customer.setStatus(AppConstant.InactiveStatus);
		Customer updateCustomer=customerRepository.save(customer);
		logger.info("Delete customer operation is performed in Customer Management");
		return ResponseEntity.ok(new MessageResponse("Customer Deleted successfully!"));
	}
	
	
}
