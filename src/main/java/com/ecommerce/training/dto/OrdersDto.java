package com.ecommerce.training.dto;


import java.util.HashMap;

import java.util.Map;

import lombok.Data;

@Data
public class OrdersDto {
private Long cutomerid;
private String orderNumber;
private Boolean isOutbound;
private  Map<Long,Long> productDetails = new HashMap<>(); 

private String description;



public OrdersDto() {
	
}




}
