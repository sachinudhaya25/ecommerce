package com.ecommerce.training.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="name")
    private String name;
	@Column(name="description")
    private String description;
	@Column(name="yop")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnore
    private LocalDateTime yop;
	@Column(name="bookType")
	private String bookType;
	@Column(name="status")
    private int status;
	@Column(name="year")
    private int year;
	
     
}
