package com.ecommerce.training.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BillDto implements Serializable{


	private static final long serialVersionUID = 1L;
	private Long ordersId;
	private String billReference;
	private Boolean isFullyDelivered;
	private Boolean isInboundBill;
	private List<DeliveredProducts> deliveredProducts;
	
	@Data
   	public static class DeliveredProducts implements Serializable
   	{	
		private static final long serialVersionUID = 1L;
		private Long productsId;
		private Long deliveredQty;
   	}
	
	
}
