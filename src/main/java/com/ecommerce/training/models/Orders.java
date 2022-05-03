package com.ecommerce.training.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBlobType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBlobType.class)
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="description")
	private String description;
	@Column(name="orderNumber")
	private String orderNumber;
	
	@Column(name="tax")
	private Double tax;
	@Column(name="totalvalue")
	private Double totalvalue;
	@Column(name="productsPrice")
	private Double productsPrice;
	@Column(name="ordered_at")
	private LocalDateTime orderedAt;
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name="createdBy")
	private Long createdBy;
	
    @Column(name="updatedBy")
   	private Long updatedBy;
    
	@Enumerated(EnumType.STRING)
    @Column(name="status")
    private OrderStatus orderStatus;
 
    @Type(type = "jsonb")
    @Column(name="order_product") 
    private List<Orderproduct> orderproduct;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    
    @Column(name="isFullyDelivered")
   	private Boolean isFullyDelivered=false;
    
    @Column(name="isOutbound")
   	private Boolean isOutbound;
    
//    @Column(name="orderedDate")
//	@Temporal(TemporalType.DATE)
//	private Date orderedDate=new Date(System.currentTimeMillis());
//	@Column(name="orderedTime")
//	@Temporal(TemporalType.TIME)
//	private Date orderedTime=new Date(System.currentTimeMillis());
    
//	@Column(name="isFullOrder")
//	private Boolean isFullOrder=false;

    @Data
	public static class Orderproduct implements Serializable
    {
    	@JsonProperty("quantity")
    	private Long quantity;
    	@JsonProperty("deliveredQty")
    	private Long deliveredQty;
    	@JsonProperty("product")
    	@ManyToMany
    	@JoinColumn(name="product_id")
    	private Product product;
    	@JsonProperty("priceTotal")
    	private Double priceTotal;
    	@JsonProperty("productsTax")
    	private Double productsTax;
    	@JsonProperty("totalAmount")
    	private Double totalAmount;
		
    }
    

	
	
	

}
