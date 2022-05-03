package com.ecommerce.training.service;




import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ecommerce.training.dto.BillDto;
import com.ecommerce.training.dto.PaymentRequestDto;
import com.ecommerce.training.models.Bill;




public interface BillService {


	ResponseEntity<?> saveBill(BillDto billDto) throws Exception;
	List<Bill> getAllBills();
	Bill getBillById(Long id);
	ResponseEntity<?> updateBill(Bill bill,Long id);
	ResponseEntity<?> updatePayment(PaymentRequestDto paymentRequestDto);
	
	public List<Bill> getBillByOrdersId(Long id);
	public Optional<Bill> getBillByBillno(String billNo);
	List<Bill> searchBills(String data, boolean isInbound) throws Exception;
	List<Bill> getBillsByDate(Date fromDate, Date toDate);
	public Bill getBillByBillRefference(String billRefference);
}
