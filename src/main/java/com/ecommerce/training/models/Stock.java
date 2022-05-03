package com.ecommerce.training.models;



import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="remarks")
	private String remarks;
	@Column(name="createdAt")
   	private LocalDateTime createdAt;
    @Column(name="updatedAt")
   	private LocalDateTime updatedAt;
    @Column(name="createdBy")
	private Long createdBy;
    @Column(name="updatedBy")
   	private Long updatedBy;
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	@Column(name="stockQuantity")
	private Long stockQuantity;

	
}
