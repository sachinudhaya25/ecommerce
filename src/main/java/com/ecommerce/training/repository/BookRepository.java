package com.ecommerce.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.training.models.Book;


public interface BookRepository extends JpaRepository<Book,Long>,BookRepositoryCustom{

}
