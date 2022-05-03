package com.ecommerce.training.service;

import java.util.List;

import javax.validation.Valid;

import com.ecommerce.training.models.Category;



public interface CategoryService {

	public List<Category> getAllCategories();
	public Category getCategoryById(Long id);
	public Category saveCategory(@Valid Category category);
	public Category updateCategory(Category stock, Long id);
	
}
