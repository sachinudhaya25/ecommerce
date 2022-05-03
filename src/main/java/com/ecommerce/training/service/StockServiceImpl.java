package com.ecommerce.training.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.training.dto.StockRequestDto;
import com.ecommerce.training.dto.StockResponseDto;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.models.Stock;
import com.ecommerce.training.models.User;
import com.ecommerce.training.repository.StockRepository;
import com.ecommerce.training.repository.UserRepository;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<Stock> getAllStocks() {
		// TODO Auto-generated method stub
		return stockRepository.findAll();
	}

	@Override
	public Stock getStockById(Long id) {

		return stockRepository.findById(id).get();
	}

	@Override
	public Stock saveStocks(@Valid StockRequestDto stockRequestDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Optional<User> getUser = userRepository.findByUsername(username); // this is for get current user id
		User user = getUser.get();
		/*******************************************************/
		LocalDateTime now = LocalDateTime.now();

		Stock stockData = stockRepository.findByProduct_id(stockRequestDto.getProductId());
		if (stockData != null) {
			stockData.setUpdatedAt(now);
			stockData.setUpdatedBy(user.getId());
			stockData.setRemarks(stockRequestDto.getRemarks());
			stockData.setStockQuantity(stockRequestDto.getProductQuantity() + stockData.getStockQuantity());
			stockRepository.save(stockData);
			return stockData;
		}
		Product product = productService.findProductById1(stockRequestDto.getProductId());
		/*******************************************************/
		Stock stock = new Stock();
		stock.setProduct(product);
		stock.setStockQuantity(stockRequestDto.getProductQuantity());
		stock.setRemarks(stockRequestDto.getRemarks());
		stock.setCreatedAt(now);
		stock.setCreatedBy(user.getId());
		stockRepository.save(stock);
		return stock;
	}

	@Override
	public Stock updateStock(Stock stock, Long id) {
		Stock stockData = stockRepository.findById(id).get();
		stockRepository.save(stock);
		return stock;
	}

	@Override
	public List<StockResponseDto> searchStocks(Long id) {
		List<StockResponseDto> stockResponseDto=new ArrayList<>();
		if(id==null)
		{
			
			List<Stock> stock= stockRepository.findAll();
			for(Stock stockData:stock)
			{
				StockResponseDto stockResponseDtoData=new StockResponseDto();
				stockResponseDtoData.setProductId(stockData.getId());
				stockResponseDtoData.setName(stockData.getProduct().getName());
				stockResponseDtoData.setProductQuantity(stockData.getStockQuantity());
				stockResponseDtoData.setUnit(stockData.getProduct().getUnit());
				stockResponseDto.add(stockResponseDtoData);
			}
			return stockResponseDto;
		}
		Stock stock= stockRepository.findById(id).get();
		StockResponseDto stockResponseDtoData=new StockResponseDto();
		stockResponseDtoData.setProductId(stock.getId());
		stockResponseDtoData.setName(stock.getProduct().getName());
		stockResponseDtoData.setProductQuantity(stock.getStockQuantity());
		stockResponseDtoData.setUnit(stock.getProduct().getUnit());
		stockResponseDto.add(stockResponseDtoData);
		return stockResponseDto;
	}

}
