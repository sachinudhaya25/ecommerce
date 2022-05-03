package com.ecommerce.training.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.training.dto.OrdersDto;
import com.ecommerce.training.models.OrderStatus;
import com.ecommerce.training.models.Orders;


@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long>,JpaSpecificationExecutor<Orders> {

	//Orders save(OrdersDto orders);
	//List<Orders> findByOrderStatus(OrderStatus status);
    //List<Orders> findByCustomer_id(Long id);
   
//    List<Orders> findByOrderedDate(Date date);
//    List<Orders> findByOrderedTime(LocalDate time);
    
    
  //  List<Orders> findByOrderedAt(LocalDateTime date);
    
//    @Query(value = "SELECT * FROM orders o WHERE o.customer_id = ?1 OR o.status=?2 OR  o.ordered_at=?3",nativeQuery = true)
//    List<Orders> findByIdAndOrderStatusAndOrderedAt(Long id,String status,String date);
//    @Query(value = "SELECT * FROM orders o WHERE o.customer_id = ?1 OR  o.ordered_at=?2",nativeQuery = true) 
//    List<Orders> findByCustIdAndOrderedAt(long customerId, String date);
//    @Query(value = "SELECT * FROM orders o WHERE o.customer_id = ?1 OR  o.status=?2",nativeQuery = true) 
//	List<Orders> findByCustIdAndOrderStatus(long customerId, String status);
//    @Query(value = "SELECT * FROM orders o WHERE o.ordered_at = ?1 OR  o.status=?2",nativeQuery = true) 
//	List<Orders> findByOrderedAtAndOrderStatus(String date, String status);
//    @Query(value = "SELECT * FROM orders o WHERE date(o.ordered_at) = ?1",nativeQuery = true)
//	List<Orders> findOrderedAt(String date);
	List<Orders> findByisOutbound(Boolean isOutbound);
}

