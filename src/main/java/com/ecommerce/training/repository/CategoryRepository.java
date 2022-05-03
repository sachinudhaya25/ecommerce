package com.ecommerce.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.training.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

	
}
