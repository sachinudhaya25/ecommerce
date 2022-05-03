package com.ecommerce.training.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.training.dto.OrdersDto;
import com.ecommerce.training.models.OrderStatus;
import com.ecommerce.training.models.Orders;




public interface OrdersService {

	    Orders findById(Long id);
	    //List<Orders> findByCustomer(Long id);
	    Orders saveOrders(OrdersDto ordersDto);
		List<Orders> getAllOrders();
		ResponseEntity<Orders> updateOrders(Orders orders,Long id);
		//List<Orders> findByStatus(OrderStatus status);
		ResponseEntity<?> changeStatusClosed(Long id);
		
		List<Orders> searchOrders(String data, boolean isOutbound);
    	
    	//ResponseEntity<?> findOrdersBillById(Long id);
    	
    	
//    	List<Orders> getByTime(LocalDate time);
    //	List<Orders> getByDate(String date);
    	
    	
		
		
}
