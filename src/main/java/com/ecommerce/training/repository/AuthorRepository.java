package com.ecommerce.training.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ecommerce.training.models.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author,Long>{

}
