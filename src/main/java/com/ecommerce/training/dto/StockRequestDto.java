package com.ecommerce.training.dto;

import lombok.Data;

@Data
public class StockRequestDto {

	private Long productId;
	private Long productQuantity;
	private String Remarks;
	
}
