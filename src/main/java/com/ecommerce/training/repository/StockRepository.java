package com.ecommerce.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ecommerce.training.models.Bill;
import com.ecommerce.training.models.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long>,JpaSpecificationExecutor<Bill> {

	public Stock findByProduct_id(Long id);
	
}
