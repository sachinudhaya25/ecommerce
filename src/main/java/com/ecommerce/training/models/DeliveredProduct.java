package com.ecommerce.training.models;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeliveredProduct implements Serializable {

	@JsonProperty("deliveredQty")
	private Long deliveredQty;
	@JsonProperty("product")
	@ManyToMany
	@JoinColumn(name="product_id")
	private Product product;
	@JsonProperty("taxAmount")
	private Double taxAmount;
	@JsonProperty("totalValue")//value=tax +price
	private Double totalValue;
	@JsonProperty("price")
	private Double price;
	
}
