package com.ecommerce.training.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.models.Book;


public interface BookService {
	        Book saveBooks(Book book);
			List<Book> getAllBooks();
			Book getBookById(Long id);
			ResponseEntity<Book> updateBook(Book book,Long id);
			ResponseEntity<?>  deleteBook(Long id);
			GenericResponse pagination(Pageable page);
			List<Book> getBooksByQueryDsl(Integer year);
			
}
