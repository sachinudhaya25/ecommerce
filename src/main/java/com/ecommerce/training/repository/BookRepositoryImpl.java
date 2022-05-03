package com.ecommerce.training.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ecommerce.training.models.Book;
import com.ecommerce.training.models.QBook;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;

public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	public static QBook qBook = QBook.book;
	
	///Project->right click ->run as -> maven  generate resource

	@Override
	public List<Book> getAllBooksByQueryDsl(Integer year) {
	
		//query dsl
		JPAQuery<Book> jpaQuery = new JPAQuery<>(em);
		//select * from book where year=2015;
		
		//method:1 normal
		
//		return jpaQuery
//			 	.from(qBook)
//			 	.where(qBook.year.eq(year))
//			 	.fetch();
		
		//method2: select particular fields using tuple
		
//		List<Tuple> tuples=jpaQuery
//			.select(qBook.id,qBook.bookType) 
//			.from(qBook)
//		 	.where(qBook.year.eq(year))
//		 	.fetch();
//		List<Book> books=new ArrayList<>();
//		for(Tuple tuple:tuples)
//		{
//			Book book=new Book();
//			book.setId(tuple.get(qBook.id));
//			book.setBookType(tuple.get(qBook.bookType));
//			books.add(book);
//		}  
//		return books;
		
		//method:3 using projection
		QBean<Book> bookQBean = Projections.bean(Book.class, qBook.id,qBook.bookType);
		List<Book> books= jpaQuery
	 	.from(qBook)
	 	.where(qBook.year.eq(year))
	 	.select(bookQBean)
	 	.fetch();
		return books;
	}

}
