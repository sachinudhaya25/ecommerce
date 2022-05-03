package com.ecommerce.training.models;



import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="name")
	@NotBlank(message = "Name is mandatory")
    private String name;
	@Column(name="email")
	@NotBlank(message = "Email is mandatory")
    private String email;
	@Column(name="phone")
    private Long phone;
	@Column(name="address")
	private String address;
	@Column(name="status")
    private int status;
//	
//	@OneToMany(mappedBy="customer")
//	private List<Orders> orders;
	
	public Customer() {
		
	}


	public Customer(Long id, String name, String email, Long phone, String address, int status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.status = status;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
