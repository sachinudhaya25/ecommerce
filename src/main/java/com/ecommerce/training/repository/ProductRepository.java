package com.ecommerce.training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.training.models.Product;





public interface ProductRepository extends JpaRepository<Product,Long> {
	
	List<Product> findAllByStatus(int status);

	Product findByIdAndStatus(Long id,int status);
	List<Product> findByName(String name);
	List<Product> findByNameStartingWith(String Name);
	List<Product> findByNameContaining(String name);	
	Page<Product> findAllByStatus(int status, Pageable pageable);
	Page<Product> findByNameContaining(String title, Pageable pageable);
	//order by
	List<Product> OrderByIdDesc();
	List<Product> OrderByIdAsc();
	
}
