package com.ecommerce.training.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.models.Category;

import com.ecommerce.training.service.CategoryService;
import com.ecommerce.training.service.ProductServiceImpl;


@RestController
@RequestMapping("/api/category")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("/get")
	public List<Category> getAllCategory()
	{   
		logger.info("Get all category method start....");
		return categoryService.getAllCategories();
		
	}
	@GetMapping("/get/{id}")
	public Category getCategoryById(@PathVariable Long id)
	{
		logger.info("Category by id method start....");
		return categoryService.getCategoryById(id);
	}
	@PostMapping("/save")
	public Category saveCategory(@Valid @RequestHeader("Authorization") String authorization, @RequestBody Category category)
	{
		
		logger.info("save category method start....");
		return categoryService.saveCategory(category); 
	}
	@PutMapping("/update/{id}")
	public Category updateCategory(@RequestBody Category category,@PathVariable Long id)
	{
		logger.info("update category method start....");
		return categoryService.updateCategory(category, id);
	}
}
