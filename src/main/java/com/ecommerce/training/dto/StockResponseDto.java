package com.ecommerce.training.dto;

import lombok.Data;

@Data
public class StockResponseDto {

	private Long productId;
	private String name;
	private Long productQuantity;
	private String unit;
	
}
