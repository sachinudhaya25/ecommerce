package com.ecommerce.training.dto;

import lombok.Data;

@Data
public class ProductDto {

	private Long id;
	private Long price;
	private String name;
	private String picture;
    private String description;
    private Double gst;
    private Long unit;

	
}
