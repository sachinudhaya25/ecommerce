package com.ecommerce.training.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.training.config.Errors;
import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.config.Pagination;
import com.ecommerce.training.dto.BookDto;
import com.ecommerce.training.exception.BadRequestException;
import com.ecommerce.training.exception.DataNotNullException;
import com.ecommerce.training.models.Book;
import com.ecommerce.training.repository.BookRepository;
import com.ecommerce.training.validator.BookValidator;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	private BookValidator bookValidator;
	@Override
	public Book saveBooks(Book book) {
		List<Errors> errors=bookValidator.validateRequest(book);
		System.out.println(errors.size());
		if(errors.size() > 0)
		{
			throw new BadRequestException("Bad Request",errors);
		}
		LocalDateTime now = LocalDateTime.now();
		book.setYop(now);
		return bookRepository.save(book);
	}

	@Override
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book getBookById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Book> updateBook(Book book, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<?> deleteBook(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse pagination(Pageable page) {
		Page<Book> book=bookRepository.findAll(page);
		GenericResponse response=new GenericResponse();
		BookDto bookDto=new BookDto();
		List<Book> bookList=book.getContent();
		Pagination pagination=Pagination.createPagination(book);
		bookDto.setPagination(pagination);
		bookDto.setBook(bookList);
		response.setData(bookDto);
		response.setStatus(HttpStatus.OK.name());
		return response;
	}

	@Override
	public List<Book> getBooksByQueryDsl(Integer year) {
		
		return bookRepository.getAllBooksByQueryDsl(year);
	}

	

}
