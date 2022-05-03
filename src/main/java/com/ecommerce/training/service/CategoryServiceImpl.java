package com.ecommerce.training.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.training.models.Category;
import com.ecommerce.training.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category getCategoryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category saveCategory(@Valid Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category updateCategory(Category stock, Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
