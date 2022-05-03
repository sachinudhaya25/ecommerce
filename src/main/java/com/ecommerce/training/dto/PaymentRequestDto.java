package com.ecommerce.training.dto;



import java.io.Serializable;

import lombok.Data;

@Data
public class PaymentRequestDto implements Serializable{


	private String paymentMethod;
	private Long billId;
	private Double amount;
	
	
}
