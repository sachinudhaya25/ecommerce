package com.ecommerce.training.controller;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.training.config.BillPDFExporter;
import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.BillDto;
import com.ecommerce.training.dto.BillResponseDto;
import com.ecommerce.training.dto.PaymentRequestDto;
import com.ecommerce.training.dto.ProductDto;
import com.ecommerce.training.models.Bill;
import com.ecommerce.training.models.Orders;
import com.ecommerce.training.service.BillService;
import com.ecommerce.training.service.ProductServiceImpl;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/api/bill")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class BillController {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	
	@Autowired
	private BillService billService;
	
	@PostMapping("/save")
	public ResponseEntity<?> saveBill(@RequestHeader("Authorization") String authorization,@RequestBody BillDto billDto) throws Exception
	{	
		logger.info("save bill method start....");
		return billService.saveBill(billDto);  
	}
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getById(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
	{
		Bill bill= billService.getBillById(id);
		return ResponseEntity.status(HttpStatus.OK).body(bill);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBill(@RequestBody Bill bill,@PathVariable Long id)
	{
		logger.info("Update Bil method start....");
		return billService.updateBill(bill, id);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllBills()
	{
		logger.info("Get all bills method start....");
		List<Bill> bills= billService.getAllBills();
		return ResponseEntity.status(HttpStatus.OK).body(bills);
	}
	@PostMapping("/updatePayment")
	ResponseEntity<?> updatePayment(@RequestHeader("Authorization") String authorization,@RequestBody PaymentRequestDto paymentRequestDto){
		logger.info("update payment method start....");
		return billService.updatePayment(paymentRequestDto);
	}
	
	//
	@GetMapping("/billInvoice/export")
    public void exportToPDF(@RequestHeader("Authorization") String authorization,@RequestParam String billRefference, HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bills_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
         
         Bill bill = billService.getBillByBillRefference(billRefference);
         
        BillPDFExporter exporter = new BillPDFExporter(bill);
        exporter.export(response);
         
    }
	@GetMapping("/billInvoice/get")
	public BillResponseDto getbillInvoice(@RequestHeader("Authorization") String authorization,@RequestParam String billRefference)
	{
		Bill bill = billService.getBillByBillRefference(billRefference);
		BillResponseDto billResponseDto=new BillResponseDto();
		billResponseDto.setName(bill.getOrders().getCustomer().getName());
		billResponseDto.setAddress(bill.getOrders().getCustomer().getAddress());
		billResponseDto.setInvoiceNumber(bill.getBillNo());
		billResponseDto.setOrderNumber(bill.getOrders().getOrderNumber());
		billResponseDto.setDate(bill.getBillDate());
		billResponseDto.setDeliveredProduct(bill.getDeliveredProduct());
		billResponseDto.setSubTotal(bill.getBillAmount());
		billResponseDto.setTax(bill.getTax());
		billResponseDto.setInvoiceTotal(bill.getTotalBillAmount());
		return billResponseDto;
	}
	
	@GetMapping("/searchDateBetween")
	public List<Bill> searchByDate(@RequestHeader("Authorization") String authorization,
								   @RequestParam(required = false) 
			                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			                       @RequestParam(required = false)
			                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate)
	{
		return billService.getBillsByDate(fromDate,toDate);
	}
	
	//sample input = {"orders id":"4","start date":"2022-02-05","end date":"2022-02-06","status":"Paid","customer id":""}
	@GetMapping("/searchInbound")
	public List<Bill> searchInboundOrders(@RequestHeader("Authorization") String authorization,String data) throws Exception
	{
		logger.info("Get Bills by search method start....");
		return billService.searchBills(data,true);		
	}
	@GetMapping("/searchOutbound")
	public List<Bill> searchOutboundOrders(@RequestHeader("Authorization") String authorization,String data) throws Exception
	{
		logger.info("Get Bills by search method start....");
		return billService.searchBills(data,false);		
	}
	
	
}
