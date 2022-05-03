package com.ecommerce.training.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	@NotBlank(message = "Name is mandatory")
	private String name;
	@Column(name = "price")
	private Double price;
	@Column(name = "unit")
	private String unit;
	@Column(name = "picture")
	private String picture;
	@Column(name = "partnumber")
	private String partnumber;
	@Column(name = "description")
	private String description;
	@Column(name = "gst")
	private Double gst;
	@Column(name = "status")
	private int status;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	@Column(name = "stockQuantity")
	private Long stockQuantity=(long) 0;

}
