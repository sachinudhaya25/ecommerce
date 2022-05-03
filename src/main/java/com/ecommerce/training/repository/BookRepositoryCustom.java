package com.ecommerce.training.repository;

import java.util.List;

import com.ecommerce.training.models.Book;

public interface BookRepositoryCustom {
	
	public List<Book> getAllBooksByQueryDsl(Integer year);

}
