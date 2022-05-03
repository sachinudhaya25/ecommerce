package com.ecommerce.training.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.ecommerce.training.models.Orders.Orderproduct;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBlobType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBlobType.class)
public class Bill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="billNo")
	private String billNo;
   
    @ManyToOne
	@JoinColumn(name="orders_id")
	private Orders orders;
    @Column(name="billAmount")
	private Double billAmount;
    @Column(name="tax")
	private Double tax;
    @Column(name="totalBillAmount")
   	private Double totalBillAmount;
    @Column(name="billDate")
    @Temporal(TemporalType.DATE)
	private Date billDate=new Date(System.currentTimeMillis());
  
    @Column(name="createdAt")
   	private LocalDateTime createdAt;
    @Column(name="updatedAt")
   	private LocalDateTime updatedAt;
    @Column(name="createdBy")
	private Long createdBy;
    @Column(name="updatedBy")
   	private Long updatedBy;
    
    @Type(type = "jsonb")
    @Column(name="delivered_Product") 
    private List<DeliveredProduct> deliveredProduct;
    @Type(type = "jsonb")
    @Column(name="payment") 
    private List<Payment> payment;
    //list of payment
    @Column(name="status") 
    private BillStatus status;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    @Column(name="isInboundBill")
   	private Boolean isInboundBill;
    
    
//    @Data
//   	public static class DeliveredProduct implements Serializable
//   	{
//
////    	@JsonProperty("orderedQty")
////    	private Long orderedQty;
//    	@JsonProperty("deliveredQty")
//    	private Long deliveredQty;
//    	@JsonProperty("product")
//    	@ManyToMany
//    	@JoinColumn(name="product_id")
//    	private Product product;
//    	@JsonProperty("taxAmount")
//    	private Double taxAmount;
//    	@JsonProperty("totalValue")//value=tax +price
//    	private Double totalValue;
//    	@JsonProperty("price")
//    	private Double price;
//    	
//    	
//    	
//   	}
    
    @Data
   	public static class Payment implements Serializable
   	{


    	@JsonProperty("paymentDate")
       	private LocalDateTime paymentDate;
    	
    	@JsonProperty("paymentMethod")
    	private String paymentMethod;
    	@JsonProperty("amount")
    	private Double amount;
    	@JsonProperty("currentBalance")
    	private Double currentBalance;
    	
   	
    	
   	}

}
