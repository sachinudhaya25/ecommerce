package com.ecommerce.training.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.training.dto.BillDto;
import com.ecommerce.training.dto.BillDto.DeliveredProducts;
import com.ecommerce.training.dto.PaymentRequestDto;
import com.ecommerce.training.exception.InvalidInputException;
import com.ecommerce.training.exception.ResourceNotFoundException;
import com.ecommerce.training.models.Bill;

import com.ecommerce.training.models.Bill.Payment;
import com.ecommerce.training.models.BillStatus;
import com.ecommerce.training.models.DeliveredProduct;
import com.ecommerce.training.models.Orders;
import com.ecommerce.training.models.Orders.Orderproduct;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.models.Stock;
import com.ecommerce.training.models.User;
import com.ecommerce.training.payload.response.MessageResponse;
import com.ecommerce.training.repository.BillRepository;
import com.ecommerce.training.repository.OrdersRepository;
import com.ecommerce.training.repository.ProductRepository;
import com.ecommerce.training.repository.StockRepository;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.specification.BillsSpecification;
import com.ecommerce.training.specification.SearchCriteria;
import com.ecommerce.training.utility.AppConstant;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private StockRepository stockRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public ResponseEntity<?> saveBill(BillDto billDto) throws Exception {
		// *********************************//
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Optional<User> getUser = userRepository.findByUsername(username); // this is for get current user id
		User user = getUser.get();
		// *********************************//

		LocalDateTime now = LocalDateTime.now();
		Date nowDate = new Date(System.currentTimeMillis());
		Bill bill = new Bill();
		Orders orders = ordersService.findById(billDto.getOrdersId());

		List<DeliveredProduct> deliveredProduct = new ArrayList<>();

		if (billDto.getIsFullyDelivered() == true) {
			if (orders.getIsFullyDelivered() == true) {
				throw new InvalidInputException("The order id " + orders.getId() + " is already Delivered");
			}
		}

		/** Start payment **/
		List<Payment> payments = new ArrayList<>();
		Payment payment = new Payment();
		payment.setPaymentDate(now);
		payment.setCurrentBalance(orders.getTotalvalue());
		payment.setAmount(0.0);
		payments.add(payment);
		/** End payment **/

		
		/** set the data **/
		bill.setCreatedAt(now);
		bill.setOrders(orders);
		bill.setBillDate(nowDate);
		bill.setCustomer(orders.getCustomer());

		bill.setPayment(payments);
		bill.setBillNo(billDto.getBillReference());
		
		if(billDto.getIsInboundBill()==orders.getIsOutbound())
		{
			throw new InvalidInputException(
					"The Mention order id is " + billDto.getOrdersId() + " is different order from order table");
		}else {
			bill.setIsInboundBill(billDto.getIsInboundBill());
		}

		bill.setCreatedBy(user.getId());
		Optional<Bill> getBillNo = getBillByBillno(billDto.getBillReference());
		if (!getBillNo.isEmpty()) {
			throw new InvalidInputException(
					"The Bill refference number " + billDto.getBillReference() + " is already exist");
		}

		if (billDto.getIsFullyDelivered() == false) {
			if (orders.getIsFullyDelivered() == true) {
				throw new InvalidInputException("The order id " + orders.getId() + " is already Delivered");
			}

			List<Bill> getBills = getBillByOrdersId(billDto.getOrdersId());

			System.out.println(getBills);
			List<DeliveredProducts> deliveredProducts = billDto.getDeliveredProducts();
			List<Orderproduct> orderProducts = orders.getOrderproduct();
			/************** Start ******************/
			/* This condition checks allready orders id in bill table */
			if (!getBills.isEmpty()) {
				List<Long> countDQty = new ArrayList<>();
				
				Double billAmount=(double) 0;
				Double billTaxAmount=(double) 0;
				List<Long> qtyDatas=new ArrayList<>();
				List<Long> pIdDatas=new ArrayList<>();
				for (DeliveredProducts dp : deliveredProducts) {
					Boolean isExist1 = false;
					Boolean isExist2 = false;
					Boolean isExist3 = false;
					Long productId = dp.getProductsId();
					Long productQty = dp.getDeliveredQty();

					Long qtyValue = (long) 0;
					Long dQtyinOrder = (long) 0;

					Double productPrice = (double) 0;
					Long dQty = (long) 0;
					Double gst = (double) 0;

					for (Orderproduct orderProduct : orderProducts) {
						Long pIdinOrder = orderProduct.getProduct().getId();
						Long qtyinOrder = orderProduct.getQuantity();
						dQtyinOrder = orderProduct.getDeliveredQty();
						if (pIdinOrder == productId) {
							isExist1 = true;
						}
						if (productQty < qtyinOrder) {
							isExist2 = true;
						}
						if ((productQty + dQtyinOrder) <= qtyinOrder) {
							isExist3 = true;
						}

						
					
						dQty = productQty;
						
					}
					qtyValue = productQty + dQtyinOrder;
					countDQty.add(qtyValue);
					if (isExist1 != true) {
						throw new ResourceNotFoundException("Mentioned Product id " + productId + " not in orders");
					}
					if (isExist2 != true) {
						throw new ResourceNotFoundException("Mentioned Product " + productId + " quantity " + productQty
								+ " is too much as in orders delivered quantity");
					}
					if (isExist3 != true) {
						throw new ResourceNotFoundException("Mentioned Product " + productId + " quantity " + productQty
								+ " is too much as in orders quantity");
					}
					DeliveredProduct dProduct = new DeliveredProduct();
					dProduct.setDeliveredQty(productQty);
					dProduct.setProduct(productService.findProductById1(productId));
					productPrice = dProduct.getProduct().getPrice();
					gst = (productPrice * dProduct.getProduct().getGst()) / 100;
					Double taxAmount = (gst * dQty);
					Double price = dQty * productPrice;
					Double totalValue = price + taxAmount;

					dProduct.setPrice(price);
					dProduct.setTotalValue(totalValue);
					dProduct.setTaxAmount(taxAmount);

					deliveredProduct.add(dProduct);
					
					billAmount=billAmount+price;
					billTaxAmount=billTaxAmount+taxAmount;
					System.out.println("bill amount***"+billAmount+"tax amount******"+billTaxAmount);
					
					/***********************************************/
					//check stock
				//	Stock stock=stockService.getStockById(dProduct.getProduct().getId());
					Product productData=productRepository.findById(dProduct.getProduct().getId()).get();
					Long pIdData=dProduct.getProduct().getId();
					Long qtyData1=dProduct.getDeliveredQty();
					Long qtyData2=productData.getStockQuantity();
					if(qtyData1>qtyData2)
					{
						throw new ResourceNotFoundException("Mentioned Product id " + pIdData + " stock is not available");
					}
					qtyDatas.add(qtyData1);
					pIdDatas.add(pIdData);
					
					/***********************************************/
				}
				bill.setBillAmount(billAmount);
				bill.setTax(billTaxAmount);
				bill.setTotalBillAmount(billAmount+billTaxAmount);
				
				System.out.println(countDQty);
				int count = 0;
				BillStatus billStatus = BillStatus.Paid;
				for (Orderproduct orderProductData1 : orderProducts) {
					if (orderProductData1.getQuantity() != countDQty.get(count))// this if loop for bill status
					{
						billStatus = BillStatus.Pending;
					}
					orderProductData1.setDeliveredQty(countDQty.get(count));

					count++;
				}
				System.out.println("***");
				orders.setOrderproduct(orderProducts);
				ordersRepository.save(orders);
				bill.setStatus(billStatus);
				bill.setDeliveredProduct(deliveredProduct);

				billRepository.save(bill);
				/***********************************************/
				for(int i=0;i<qtyDatas.size();i++)
				{
					Long qtyData2=qtyDatas.get(i);
					Long pIdData1=pIdDatas.get(i);
					Product productData=productRepository.findById(pIdData1).get();
					Long qtyData3=productData.getStockQuantity();
					System.out.println("qtyData3 "+qtyData3+"qtyData2 "+qtyData2);
					if(billDto.getIsInboundBill()==true)
					{
						productData.setStockQuantity(qtyData3+qtyData2);
					}else {
						productData.setStockQuantity(qtyData3-qtyData2);	
					}					
					productRepository.save(productData);	
					
				}
				
				/***********************************************/
				return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Added Successfully"));

			}
			/**************** End ****************/

			/*
			 * This for loop is for find Mentioned Product id and qty is there or not in
			 * orders products
			 */
			/*------------Start-------------- */

			Double billAmount=(double) 0;
			Double billTaxAmount=(double) 0;
			List<Long> qtyDatas=new ArrayList<>();
			List<Long> pIdDatas=new ArrayList<>();
			for (DeliveredProducts dp : deliveredProducts) {
				Boolean isExist1 = false;
				Boolean isExist2 = false;
				Long productId = dp.getProductsId();
				Long productQty = dp.getDeliveredQty();

				Double productPrice = (double) 0;
				Long dQty = (long) 0;
				Double gst = (double) 0;

				for (Orderproduct orderProduct : orderProducts) {
					Long pIdinOrder = orderProduct.getProduct().getId();
					Long qtyinOrder = orderProduct.getQuantity();
					if (pIdinOrder == productId) {
						isExist1 = true;
					}
					if (productQty <= qtyinOrder) {
						isExist2 = true;
					}
					orderProduct.setDeliveredQty(productQty);//

				
					dQty = productQty;
					

				}
				if (isExist1 != true) {
					throw new ResourceNotFoundException("Mentioned Product id " + productId + " not in orders");
				}
				if (isExist2 != true) {
					throw new ResourceNotFoundException(
							"Mentioned Product quantity " + productQty + " is too much as in orders quantity");
				}
				DeliveredProduct dProduct = new DeliveredProduct();
				dProduct.setDeliveredQty(productQty);
				dProduct.setProduct(productService.findProductById1(productId));
				productPrice = dProduct.getProduct().getPrice();
				gst = (productPrice * dProduct.getProduct().getGst()) / 100;
				Double taxAmount = (gst * dQty);
				Double price = dQty * productPrice;
				Double totalValue = price + taxAmount;

				dProduct.setPrice(price);
				dProduct.setTotalValue(totalValue);
				dProduct.setTaxAmount(taxAmount);

				deliveredProduct.add(dProduct);
				
				billAmount=billAmount+price;
				billTaxAmount=billTaxAmount+taxAmount;
				System.out.println("bill amount***"+billAmount+"tax amount******"+billTaxAmount);
				
				/***********************************************/
				//check stock
				//Stock stock=stockService.getStockById(dProduct.getProduct().getId());
				Product productData=productRepository.findById(dProduct.getProduct().getId()).get();
				Long pIdData=dProduct.getProduct().getId();
				Long qtyData1=dProduct.getDeliveredQty();
				Long qtyData2=productData.getStockQuantity();
				if(qtyData1>qtyData2)
				{
					if(orders.getIsOutbound()==true) {
					throw new ResourceNotFoundException("Mentioned Product id " + pIdData + " stock is not available");
					}
				}
				qtyDatas.add(qtyData1);
				pIdDatas.add(pIdData);
				
				/***********************************************/
				
			}
			/*------------End--------------------*/
			bill.setBillAmount(billAmount);
			bill.setTax(billTaxAmount);
			bill.setTotalBillAmount(billAmount+billTaxAmount);
			int count1 = 0;
			BillStatus billStatus = BillStatus.Paid;
			for (Orderproduct orderProductData1 : orderProducts) {
				if (orderProductData1.getQuantity() != orderProductData1.getDeliveredQty())// this if loop for bill status
				{
					billStatus = BillStatus.Pending;
				}
				count1++;
			}
			
			bill.setStatus(billStatus);
			orders.setOrderproduct(orderProducts);
			ordersRepository.save(orders);
			bill.setDeliveredProduct(deliveredProduct);
			billRepository.save(bill);
			
			/***********************************************/
			for(int i=0;i<qtyDatas.size();i++)
			{
				Long qtyData2=qtyDatas.get(i);
				Long pIdData1=pIdDatas.get(i);
				Product productData=productRepository.findById(pIdData1).get();
				Long qtyData3=productData.getStockQuantity();
				System.out.println("qtyData3 "+qtyData3+"qtyData2 "+qtyData2);
				if(billDto.getIsInboundBill()==true)
				{
					productData.setStockQuantity(qtyData3+qtyData2);
				}else {
					productData.setStockQuantity(qtyData3-qtyData2);	
				}					
				productRepository.save(productData);	
				
			}
			
			/***********************************************/
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Added Successfully"));
		}

		// This for loop for getting ordered products items from orders table to set the
		// delivered product in Bill table
		// *********************************//
		List<Long> qtyDatas=new ArrayList<>();
		List<Long> pIdDatas=new ArrayList<>();
		List<Orderproduct> orderProducts = orders.getOrderproduct();
		for (Orderproduct orderproduct : orderProducts) {
			DeliveredProduct dProduct = new DeliveredProduct();
			dProduct.setDeliveredQty(orderproduct.getQuantity() - orderproduct.getDeliveredQty());
			dProduct.setProduct(orderproduct.getProduct());

			Double productPrice = orderproduct.getProduct().getPrice();
			Long dQty = orderproduct.getQuantity() - orderproduct.getDeliveredQty();
			Double gst = (productPrice * orderproduct.getProduct().getGst()) / 100;

			Double taxAmount = (gst * dQty);
			Double price = dQty * productPrice;
			Double totalValue = price + taxAmount;

			dProduct.setPrice(price);
			dProduct.setTotalValue(totalValue);
			dProduct.setTaxAmount(taxAmount);
			
			/***********************************************/
			//check stock
			//Stock stock=stockService.getStockById(dProduct.getProduct().getId());
			Product productData=productRepository.findById(dProduct.getProduct().getId()).get();
			Long pIdData=dProduct.getProduct().getId();
			Long qtyData1=dProduct.getDeliveredQty();
			Long qtyData2=productData.getStockQuantity();
			if(qtyData1>qtyData2)
			{
				if(orders.getIsOutbound()==true) {
				throw new ResourceNotFoundException("Mentioned Product id " + pIdData + " stock is not available");
				}
			}
			qtyDatas.add(qtyData1);
			pIdDatas.add(pIdData);
			
			/***********************************************/
			
			

			deliveredProduct.add(dProduct);

			orderproduct.setDeliveredQty(orderproduct.getQuantity());
		}
		// *********************************//
		bill.setTax(orders.getTax());
		bill.setTotalBillAmount(orders.getTotalvalue());// with gst
		bill.setBillAmount(orders.getProductsPrice());// without gst
		bill.setDeliveredProduct(deliveredProduct);
		bill.setStatus(BillStatus.Paid);
		orders.setOrderproduct(orderProducts);
		orders.setIsFullyDelivered(true);
		ordersRepository.save(orders);
		billRepository.save(bill);
		/*****************/
		/***********************************************/
		System.out.println(qtyDatas);
		for(int i=0;i<qtyDatas.size();i++)
		{
			Long qtyData2=qtyDatas.get(i);
			Long pIdData1=pIdDatas.get(i);
			Product productData=productRepository.findById(pIdData1).get();
			Long qtyData3=productData.getStockQuantity();
			System.out.println("qtyData3 "+qtyData3+"qtyData2 "+qtyData2);
			if(billDto.getIsInboundBill()==true)
			{
				productData.setStockQuantity(qtyData3+qtyData2);
			}else {
				productData.setStockQuantity(qtyData3-qtyData2);	
			}					
			productRepository.save(productData);		
		}
		
		/***********************************************/
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Added Successfully"));
	}

	@Override
	public List<Bill> getAllBills() {
		return billRepository.findAll();
	}

	@Override
	public Bill getBillById(Long id) {
		return billRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bill is not exist with this id :" + id));

	}

	@Override
	public ResponseEntity<?> updateBill(Bill bill, Long id) {
		LocalDateTime now = LocalDateTime.now();
		Bill getBill = billRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bill is not exist with this id :" + id));
		bill.setUpdatedAt(now);
		billRepository.save(bill);
		return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
	}

	@Override
	public ResponseEntity<?> updatePayment(PaymentRequestDto paymentRequestDto) {
		// *********************************//
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Optional<User> getUser = userRepository.findByUsername(username); // this is for get current user id
		User user = getUser.get();
		// *********************************//
		LocalDateTime now = LocalDateTime.now();

		Bill bill = billRepository.findById(paymentRequestDto.getBillId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Bill is not exist with this id :" + paymentRequestDto.getBillId()));

		List<Payment> payments = bill.getPayment();
		Payment payment = new Payment();
		int size = payments.size();
		// Double amount=payments.get(size-1).getAmount();
		Double amount = paymentRequestDto.getAmount();
		Double currentBalance = payments.get(size - 1).getCurrentBalance();
		if (currentBalance == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("No need to pay!"));
		}
		currentBalance = currentBalance - amount;

		payment.setAmount(amount);
		payment.setCurrentBalance(currentBalance);
		payment.setPaymentDate(now);
		payment.setPaymentMethod(paymentRequestDto.getPaymentMethod());
		payments.add(payment);

		bill.setPayment(payments);
		bill.setUpdatedBy(user.getId());
		bill.setUpdatedAt(now);

		Bill updatedBill = billRepository.save(bill);
		return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
	}

	@Override
	public List<Bill> getBillByOrdersId(Long id) {
		List<Bill> bill = billRepository.findAllBillByOrders_id(id);
		return bill;
	}

	@Override
	public Optional<Bill> getBillByBillno(String billNo) {
		return billRepository.findByBillNo(billNo);
	}

	@Override
	public List<Bill> searchBills(String data,boolean isInbound) throws Exception {
		logger.info(data);
		JSONObject jsonObject;
		List<Bill> bills = null;
		String defaultValueIfNull = null;
		long ordersId = 0;
		long custmerId = 0;
		if (data == null) {
			bills = billRepository.findByIsInboundBill(isInbound);
		} else {
			try {
				jsonObject = new JSONObject(data);
				String orderID = jsonObject.optString("orders id", defaultValueIfNull);
				String custID = jsonObject.optString("customer id", defaultValueIfNull);
				String startDate = jsonObject.optString("start date", defaultValueIfNull);
				String endDate = jsonObject.optString("end date", defaultValueIfNull);
				String status = jsonObject.optString("status", defaultValueIfNull);

				if (orderID != null) {
					ordersId = Long.parseLong(orderID);
				}
				if (custID != null) {
					custmerId = Long.parseLong(custID);
				}

				BillsSpecification spec1 = new BillsSpecification(new SearchCriteria("orders", ":", ordersId));
				BillsSpecification spec2 = null;
				BillsSpecification spec3 = null;
				BillsSpecification spec4 = new BillsSpecification(new SearchCriteria("customer", ":", custmerId));
				BillsSpecification spec5 = new BillsSpecification(new SearchCriteria("isInboundBill", ":", isInbound));
				if (status != null) {
					BillStatus billStatus = (status.equals("Paid")) ? (BillStatus.Paid) : (BillStatus.Pending);
					System.out.println(billStatus);
					spec2 = new BillsSpecification(new SearchCriteria("status", ":", billStatus));
				}
				if (startDate != null && endDate != null) {
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				LocalDateTime fromDate = LocalDateTime.parse(startDate);
//				LocalDateTime toDate = LocalDateTime.parse(endDate);
					Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
					Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
					if (!fromDate.before(toDate)) {
						throw new InvalidInputException("End date grater than start date");
					}

					spec3 = new BillsSpecification(new SearchCriteria("billDate", "--", fromDate, toDate));
				}
				// bills = ordersRepository.findAll(spec2);
				
				bills = billRepository.findAll(Specification.where(spec1).or(spec2).or(spec3).or(spec4).and(spec5));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return bills;

	}

	@Override
	public List<Bill> getBillsByDate(Date fromDate, Date toDate) {
		return billRepository.findAll(new Specification<Bill>() {

			@Override
			public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate p = builder.conjunction();
				return builder.and(p, builder.between(root.get("date"), fromDate, toDate));
			}

		});
	}

	@Override
	public Bill getBillByBillRefference(String billRefference) {
		return billRepository.findByBillNo(billRefference).get();
	}

	
}
