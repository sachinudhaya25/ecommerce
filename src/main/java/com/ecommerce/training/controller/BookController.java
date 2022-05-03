package com.ecommerce.training.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.models.Book;

import com.ecommerce.training.service.BookService;
import com.ecommerce.training.service.ProductServiceImpl;
import com.ecommerce.training.validator.BookValidator;

@RestController
@RequestMapping("/api/book")
public class BookController {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	private BookService bookService;
	
	
	@GetMapping("/get")
	public List<Book> getAllBook()
	{   
		logger.info("Get all books method start....");
		return bookService.getAllBooks();
		
	}
	@GetMapping("/get/{id}")
	public Book getBookById(@PathVariable Long id)
	{
		logger.info("book by id method start....");
		return bookService.getBookById(id);
	}
	@PostMapping("/save")
	public Book saveBook(@Valid @RequestBody Book book)
	{
		
		logger.info("save book method start....");
		
		return bookService.saveBooks(book); 
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Book> updateBook(@RequestBody Book book,@PathVariable Long id)
	{
		logger.info("update book method start....");
		return bookService.updateBook(book, id);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable Long id)
	{
		logger.info("Delete book method start....");
		return bookService.deleteBook(id);
	}
	@GetMapping("/pagination")
	public GenericResponse getPagination(@RequestParam(defaultValue = "0") int page,
		      @RequestParam(defaultValue = "5") int size)
	{
		 String sort="id";
		 Pageable paging = PageRequest.of(page, size,Sort.by(sort).descending());
		logger.info("pagination method start....");
		return bookService.pagination(paging);
	}
	@GetMapping("/getbooks")
	public List<Book> getBookByQueryDsl(@RequestParam(value = "year") Integer year)
	{   
		logger.info("Get books by querydsl method start....");
		return bookService.getBooksByQueryDsl(year);
	}
}
