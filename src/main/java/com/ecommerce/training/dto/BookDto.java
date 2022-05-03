package com.ecommerce.training.dto;

import java.util.List;

import com.ecommerce.training.config.Pagination;
import com.ecommerce.training.models.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private List<Book> book;
	private Pagination pagination;
	
}
