package com.ecommerce.training.dto;

import java.util.Date;
import java.util.List;


import com.ecommerce.training.models.DeliveredProduct;

import lombok.Data;


@Data
public class BillResponseDto {
 
	private String name;
	private String address;
	private String invoiceNumber;
	private String orderNumber;
	private Date date;
    private List<DeliveredProduct> deliveredProduct;
    private Double subTotal;
    private Double tax;
    private Double invoiceTotal;
}
