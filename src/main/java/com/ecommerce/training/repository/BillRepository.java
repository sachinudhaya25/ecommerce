package com.ecommerce.training.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ecommerce.training.models.Bill;
import com.ecommerce.training.models.Orders;



@Repository
public interface BillRepository extends JpaRepository<Bill,Long>,JpaSpecificationExecutor<Bill> {

	
	
	public List<Bill> findAllBillByOrders_id(Long id);
	
	Optional<Bill> findByBillNo(String billNo);
	
	List<Bill> findByIsInboundBill(Boolean isInbound);


}
