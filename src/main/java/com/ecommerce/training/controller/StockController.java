package com.ecommerce.training.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ecommerce.training.models.Stock;
import com.ecommerce.training.service.ProductServiceImpl;
import com.ecommerce.training.service.StockService;
import com.ecommerce.training.dto.StockRequestDto;
import com.ecommerce.training.dto.StockResponseDto;


@RestController
@RequestMapping("/api/stock")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class StockController {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	private StockService stockService;
	
	
	@GetMapping("/get")
	public List<Stock> getAllStock(@RequestHeader("Authorization") String authorization)
	{   
		logger.info("Get all books method start....");
		return stockService.getAllStocks();
		
	}
	@GetMapping("/get/{id}")
	public Stock getStockById(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
	{
		logger.info("Stock by id method start....");
		return stockService.getStockById(id);
	}
	@PostMapping("/save")
	public Stock saveStock(@Valid @RequestHeader("Authorization") String authorization, @RequestBody StockRequestDto stockRequestDto)
	{
		
		logger.info("save stock method start....");
		return stockService.saveStocks(stockRequestDto); 
	}
	@PutMapping("/update/{id}")
	public Stock updateStock(@RequestHeader("Authorization") String authorization,@RequestBody Stock stock,@PathVariable Long id)
	{
		logger.info("update stock method start....");
		return stockService.updateStock(stock, id);
	}
	@GetMapping("/search")
	public List<StockResponseDto> searchStocks(@RequestHeader("Authorization") String authorization,@RequestParam(required=false) Long id) throws Exception
	{
		logger.info("Get Bills by search method start....");
		return stockService.searchStocks(id);		
	}
}
