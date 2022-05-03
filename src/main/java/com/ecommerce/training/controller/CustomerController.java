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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.models.Customer;
import com.ecommerce.training.service.CustomerService;
import com.ecommerce.training.service.ProductServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;



@RestController
//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

@RequestMapping("/api/customer")
public class CustomerController {
	CustomerService customerService;
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
//	@ApiImplicitParams({
//	    @ApiImplicitParam(name = "Authorization", value = "Bearer token", 
//	                      required = true, dataType = "string", paramType = "header") })
	@GetMapping("/get")
	public List<Customer> getAllCustomer()
	{   
		logger.info("Get all customers method start....");
		return customerService.getAllCustomers();
		
	}
	@GetMapping("/get/{id}")
	public Customer getCustomerById(@PathVariable Long id)
	{
		logger.info("Get customer by id method start....");
		return customerService.getCustomerById(id);
	}
	@PostMapping("/save")
	public Customer saveCustomer(@Valid @RequestBody Customer customer)
	{
		logger.info("save customer method start....");
		return customerService.saveCustomers(customer); 
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer,@PathVariable Long id)
	{
		logger.info("update customer method start....");
		return customerService.updateCustomer(customer, id);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id)
	{
		logger.info("Delete customer method start....");
		return customerService.deleteCustomer(id);
	}
}
