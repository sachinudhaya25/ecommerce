package com.ecommerce.training.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.OrdersDto;
import com.ecommerce.training.models.OrderStatus;
import com.ecommerce.training.models.Orders;
import com.ecommerce.training.service.OrdersService;
import com.ecommerce.training.service.ProductServiceImpl;



@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class OrdersController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	OrdersService ordersService;
	@Autowired
	public OrdersController(OrdersService ordersService) {
		this.ordersService = ordersService;
	}
	
	//sample input for save
//	{
//		  "cutomerid": 1,
//		  "description": "Test",
//		  "productDetails": {
//		    "2": 1,
//		    "3": 1
//		  }
//		}
	
	@PostMapping("/save")
	public Orders saveOrders(@RequestHeader("Authorization") String authorization,@RequestBody OrdersDto ordersDto)
	{
		logger.info("save orders method start....");
		return ordersService.saveOrders(ordersDto); 
	}
//	@GetMapping("/get/status")
//	public List<Orders> getOrdersByStatus(@RequestParam(required = false, defaultValue = "") OrderStatus status)
//	{
//		logger.info("Get Orders by Status method start....");
//		return ordersService.findByStatus(status);
//	}
	@GetMapping("/get/{id}")
	public Orders getOrdersByid(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
	{
		logger.info("Get Orders by Id method start....");
		return ordersService.findById(id);
	}
//	@GetMapping("/get_customer/{id}")
//	public List<Orders> getOrdersByCustomerId(@PathVariable Long id)
//	{
//		logger.info("Get Orders by Customer id method start....");
//		return ordersService.findByCustomer(id);
//	}

	
	
	/// sample input= {"cutomer id":"1","status":"Active","ordered date":"2022-01-25T17:42:16"}
	@GetMapping("/searchIn")
	public List<Orders> searchInboundOrders(@RequestHeader("Authorization") String authorization,String data)
	{
		logger.info("Get Orders by search method start....");
		boolean isOutbound=false;
		return ordersService.searchOrders(data,isOutbound);
		
	}
	@GetMapping("/searchOut")
	public List<Orders> searchOutboundOrders(@RequestHeader("Authorization") String authorization,String data)
	{
		boolean isOutbound=true;
		logger.info("Get Orders by search method start....");
		return ordersService.searchOrders(data,isOutbound);
		
	}
	
	
	//sample input = 2022-01-25
//	@GetMapping("/get/date")
//	public List<Orders> getOrdersByDate(@RequestParam("date") String date)
//	{
//		logger.info("Get Orders by Date method start....");
//		return ordersService.getByDate(date);
//	}
	
	
	
	
	
	
	
	
	//for billing
//	@GetMapping("/getBill/{id}")
//	public ResponseEntity<?> getOrdersBillByid(@PathVariable Long id)
//	{
//		logger.info("Get Orders by Id method start....");
//		return ordersService.findOrdersBillById(id);
//	}
//	@GetMapping("/get/time")
//	public List<Orders> getOrdersByTime(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time)
//	{
//		logger.info("Get Orders by Time method start....");
//		return ordersService.getByTime(time);
//	}
//	@GetMapping("/get/date")
//	public List<Orders> getOrdersByDate(@RequestParam("date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
//	{
//		logger.info("Get Orders by Date method start....");
//		return ordersService.getByDate(date);
//	}
}
