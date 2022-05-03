package com.ecommerce.training.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.training.dto.OrdersDto;
import com.ecommerce.training.exception.ResourceNotFoundException;
import com.ecommerce.training.models.Customer;
import com.ecommerce.training.models.OrderStatus;
import com.ecommerce.training.models.Orders;
import com.ecommerce.training.models.Orders.Orderproduct;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.models.User;
import com.ecommerce.training.payload.response.MessageResponse;
import com.ecommerce.training.repository.OrdersRepository;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.specification.OrdersSpecification;
import com.ecommerce.training.specification.SearchCriteria;


@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public OrdersServiceImpl(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@Override
	public Orders findById(Long id) {
		Orders orders = ordersRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not exist with this id :" + id));
		return orders;
	}

	@Override
	public List<Orders> getAllOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Orders> updateOrders(Orders orders, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Orders saveOrders(OrdersDto ordersDto) {
		LocalDateTime now = LocalDateTime.now();
		// Date now=new Date(System.currentTimeMillis());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username=auth.getName();
  
	    Optional<User> getUser=userRepository.findByUsername(username); // this is for get current user id
	    User user=getUser.get();
	    
		
		Orders orders = new Orders();
		orders.setOrderStatus(OrderStatus.Active);

		orders.setDescription(ordersDto.getDescription());
		// orders.setUnit(ordersDto.getUnit());
		Customer customer = customerService.getCustomerById(ordersDto.getCutomerid());
		logger.info("Customer id is " + ordersDto.getCutomerid());
		Map<Long, Long> h = ordersDto.getProductDetails();
		Set s = h.entrySet();
		Iterator i = s.iterator();
		Double TotalPrice = 0.0;
		Double Tax = 0.0;
		Double productsPrice=0.0;
		List<Orderproduct> temp = new ArrayList<>();

		while (i.hasNext()) {

			Object o = i.next();
			Map.Entry e = (Map.Entry) o;
			Long id = (Long) e.getKey();
            Long quantity = (Long) e.getValue();
			logger.info("Product id is " + id + " and quantity is " + quantity);
			
				Product product = productService.findProductById1(id);
				Double price = product.getPrice();
				Double gst = product.getGst();
				
				gst = (price * gst) / 100;
				Double priceTotal=(double) (quantity*(price));
				productsPrice=priceTotal+productsPrice;
				logger.info("Product id " + id + " price=" + price + " and GST=" + gst);
				Double total = quantity * (price + gst);
				logger.info("Product id " + id + " total=" + total);
				TotalPrice = TotalPrice + total;
				Tax = Tax + (gst * quantity);
			
						Orderproduct o2 = new Orderproduct();
						Double productsTax=gst*quantity;
						Double totalAmount=priceTotal+productsTax;
						o2.setProduct(product);
						o2.setQuantity(quantity);
						o2.setPriceTotal(priceTotal);
						o2.setProductsTax(productsTax);
						o2.setTotalAmount(totalAmount);
						o2.setDeliveredQty((long) 0);
			            temp.add(o2);
		}

		logger.info("Total price=" + TotalPrice + " and tax=" + Tax);

		orders.setTotalvalue(TotalPrice);
		orders.setProductsPrice(productsPrice);
		orders.setTax(Tax);
		// System.out.println(temp);
		orders.setOrderproduct(temp);
		orders.setCustomer(customer);
	    orders.setOrderedAt(now);
	    orders.setCreatedBy(user.getId());
		orders.setOrderedAt(now);
		orders.setOrderNumber("Order_"+ordersDto.getOrderNumber());
		orders.setIsOutbound(ordersDto.getIsOutbound());
		// orders.setOrderedTime(now);

		// return orders;
		logger.info("Orders save successfully");
		return ordersRepository.save(orders);
	}

//	@Override
//	public List<Orders> findByStatus(OrderStatus status) {
//		List<Orders> orders = ordersRepository.findByOrderStatus(status);
//		if (orders.isEmpty()) {
//			throw new ResourceNotFoundException("Orders is not exist");
//		}
//		return orders;
//	}

	@Override
	public ResponseEntity<?> changeStatusClosed(Long id) {
		Orders orders = ordersRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orders not exist with this id :" + id));
		orders.setOrderStatus(OrderStatus.Closed);
		return ResponseEntity.ok(new MessageResponse("Order Updated successfully!"));
	}

//	@Override
//	public List<Orders> findByCustomer(Long id) {
//		List<Orders> orders = ordersRepository.findByCustomer_id(id);
//		if (orders == null) {
//			throw new ResourceNotFoundException("Customer is not exist");
//		}
//		return orders;
//	}

//	@Override
//	public List<Orders> getByTime(LocalDate time) {
//		logger.info("Get by time operation is performed in orders management");
//		return ordersRepository.findByOrderedTime(time);
//	}



//	@Override
//	public List<Orders> getByDate(String date) {
//		logger.info("Get by date operation is performed in orders management");
//		return ordersRepository.findOrderedAt(date);
//	}
	@Override
	public List<Orders> searchOrders(String data, boolean isOutbound) {
		logger.info(data);
		JSONObject jsonObject;
		List<Orders> orders=null;
		
		String defaultValueIfNull=null;
		long customerId=0;
		if(data==null)
			{
				orders=ordersRepository.findByisOutbound(isOutbound);
			}else {
		try {
			jsonObject = new JSONObject(data );
			String custId = jsonObject.optString("cutomer id", defaultValueIfNull);
			String date = jsonObject.optString("ordered date", defaultValueIfNull);
			String status = jsonObject.optString("status", defaultValueIfNull);
			
			if(custId!=null)
				{
			   customerId=Long.parseLong(custId);
				}
			
			OrdersSpecification spec1 = new OrdersSpecification(new SearchCriteria("customer", ":",customerId));
			OrdersSpecification spec2=null;
			OrdersSpecification spec3=null;
			OrdersSpecification spec4 = new OrdersSpecification(new SearchCriteria("isOutbound", ":",isOutbound));
			if(status!=null) {
			OrderStatus orderStatus=(status.equals("Active")) ? (OrderStatus.Active) : (OrderStatus.Closed);
		    spec2 = new OrdersSpecification(new SearchCriteria("orderStatus", ":",orderStatus));
			}
			if(date!=null)
			{
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime localdatetime = LocalDateTime.parse(date);
				
			 spec3 = new OrdersSpecification(new SearchCriteria("orderedAt", ":",localdatetime));
			}
		    //orders = ordersRepository.findAll(spec2);
		    orders = ordersRepository.findAll(Specification.where(spec1).or(spec2).or(spec3).and(spec4));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
			}
		
		
		return orders;
	}
	

//
//	@Override
//	public ResponseEntity<?> findOrdersBillById(Long id) {
//		Orders orders= ordersRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Order not exist with this id :" + id));
//	    //orders.setIsFullOrder(true);
//		Map<String,Object> data=new LinkedHashMap<>();
//		data.put("ordersNo", "order#"+orders.getId());
//		data.put("isFullOrder", orders.getIsFullOrder());
//		data.put("Products",orders.getOrderproduct());
//		data.put("TAX", orders.getTax());
//		data.put("Total Price", orders.getTotalvalue());
//		return ResponseEntity.status(HttpStatus.OK).body(data);
//	}
//
//	@Override
//	public List<Orders> searchOrders(String data) {
//		logger.info(data);
//		JSONObject jsonObject;
//		
//		List<Orders> orders1=null;
//		String defaultValueIfNull=null;
//		if(data==null)
//		{
//			orders1=ordersRepository.findAll();
//		}else {
//		try {
//			jsonObject = new JSONObject(data );
////			String custId=jsonObject.getString("cutomer id");
////			String date=jsonObject.getString("ordered date");
////			String status=jsonObject.getString("status");
//			String custId = jsonObject.optString("cutomer id", defaultValueIfNull);
//			String date = jsonObject.optString("ordered date", defaultValueIfNull);
//			String status = jsonObject.optString("status", defaultValueIfNull);
//			System.out.println(date);
//			long customerId=0;
//			if(custId!=null)
//			{
//		    customerId=Long.parseLong(custId);
//			}
//			if(custId!=null&&date!=null&&status!=null)
//			{
//		    orders1=ordersRepository.findByIdAndOrderStatusAndOrderedAt(customerId,status,date);
//			}else if(custId!=null&&date!=null)
//			{
//		    orders1=ordersRepository.findByCustIdAndOrderedAt(customerId,date);
//			}else if(custId!=null&&status!=null)
//			{
//		    orders1=ordersRepository.findByCustIdAndOrderStatus(customerId,status);
//			}else if(date!=null&&status!=null)
//			{
//			orders1=ordersRepository.findByOrderedAtAndOrderStatus(date,status);
//			}else if(custId!=null)
//			{
//			orders1=findByCustomer(customerId);
//			}else if(date!=null)
//			{
//			orders1=getByDate(date);
//			}else if(status!=null)
//			{
//		    OrderStatus orderStatus=OrderStatus.valueOf(status);	
//			orders1=findByStatus(orderStatus);
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		}
//		
//		return orders1;
//	}

}
