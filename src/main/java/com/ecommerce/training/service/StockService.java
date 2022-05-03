package com.ecommerce.training.service;

import java.util.List;

import javax.validation.Valid;



import com.ecommerce.training.dto.StockRequestDto;
import com.ecommerce.training.dto.StockResponseDto;
import com.ecommerce.training.models.Stock;

public interface StockService {
	
	public List<Stock> getAllStocks();
	public Stock getStockById(Long id);
	public Stock saveStocks(@Valid StockRequestDto stockRequestDto);
	public Stock updateStock(Stock stock, Long id);
	public List<StockResponseDto> searchStocks(Long id);
}
