package com.ecommerce.training.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.training.config.ExcelHelper;
import com.ecommerce.training.config.ExcelHelper2;
import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.exception.ResourceNotFoundException;
import com.ecommerce.training.models.Category;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.models.Trackable;
import com.ecommerce.training.models.User;
import com.ecommerce.training.payload.response.MessageResponse;
import com.ecommerce.training.repository.ProductRepository;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.utility.AppConstant;

@Service
public class ProductServiceImpl implements ProductService {

	ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductService productService;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public GenericResponse findProductById(Long id) {
		Product product = productRepository.findByIdAndStatus(id, AppConstant.ActiveStatus);
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setDescription("Product Management");
		genericResponse.setData(product);
		genericResponse.setMessage("Success");
		genericResponse.setStatus("Active");
		logger.info("Find by id operation is performed in Product Management");
		return genericResponse;
	}

	@Override
	public ResponseEntity<?> saveProduct(Product product) {

		/*******************************************************/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username=auth.getName();
	    Optional<User> getUser=userRepository.findByUsername(username); // this is for get current user id
	    User user=getUser.get();
	    /*******************************************************/
		
		Trackable track=new Trackable();
		//Date now=new Date(System.currentTimeMillis());	
		LocalDateTime now = LocalDateTime.now();
		//track.setCreatedBy(user.getId());
		
		/*******************************************************/
		
		Category cat=new Category();
		cat.setName(product.getName());
		cat.setCreatedAt(now);
		cat.setCreatedBy(user.getId());
		
		/*******************************************************/
		product.setStatus(AppConstant.ActiveStatus);
		product.setCategory(cat);
		Product p = productRepository.save(product);
		logger.info("Save product operation is performed in Product Management");
		return ResponseEntity.ok(new MessageResponse("Product Added successfully!"));
	}

	@Override
	public void saveProduct1(Product product) {
		productRepository.save(product);

	}

	@Override
	public GenericResponse getAllProducts() {
		logger.info("Get All Product operation is performed in Product Management");
		List<Product> product = productRepository.findAllByStatus(AppConstant.ActiveStatus);
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setData(product);
		genericResponse.setDescription("Product Management");
		genericResponse.setMessage("Products");
		genericResponse.setStatus("Active");

		return genericResponse;
	}

	@Override
	public ResponseEntity<?> updateProduct(Product product, Long id) {
		Product pro = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with this id :" + id));
		productRepository.save(product);

		logger.info("Update product operation is performed in Product Management");
		return ResponseEntity.ok(new MessageResponse("Product Updated successfully!"));
	}

	@Override
	public ResponseEntity<?> deleteProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with this id :" + id));
		product.setStatus(AppConstant.InactiveStatus);
		Product updateUser = productRepository.save(product);
		logger.info("Delete product operation is performed in Product Management");
		return ResponseEntity.ok(new MessageResponse("Product Deleted successfully!"));
	}

	@Override
	public GenericResponse findByName(String Name) {

		List<Product> product = productRepository.findByName(Name);
		if (product.isEmpty()) {
			throw new ResourceNotFoundException("Product is not exist by this name");
		}
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setData(product);
		genericResponse.setDescription("Product Management");
		genericResponse.setMessage("Products");
		genericResponse.setStatus("Active");
		logger.info("Find by Name operation is performed in Product Management");
		return genericResponse;

	}

	@Override
	public GenericResponse findByNameStartingWith(String Name) {
		logger.info("Find by Name starting with operation is performed in Product Management");
		List<Product> product = productRepository.findByNameStartingWith(Name);
		if (product.isEmpty()) {
			throw new ResourceNotFoundException("product is not found by this name");
		}
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setData(product);
		genericResponse.setDescription("Product Management");
		genericResponse.setMessage("Products");
		genericResponse.setStatus("Active");
		return genericResponse;
	}

	@Override
	public ResponseEntity<?> uploadFile(MultipartFile reapExcelDataFile) throws IOException {
		// List<Product> temp = new ArrayList<>();
		try {
			XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				Product p = new Product();
				XSSFRow row = worksheet.getRow(i);
				p.setStatus(2);
				p.setName(row.getCell(0).getStringCellValue());
				p.setDescription(row.getCell(1).getStringCellValue());
				p.setPrice((Double) row.getCell(2).getNumericCellValue());
				p.setGst(row.getCell(3).getNumericCellValue());
				// p.setPicture(row.getCell(4).getStringCellValue());
				p.setPartnumber(row.getCell(4).getStringCellValue());
				// temp.add(p);
				saveProduct1(p);
				// productRepository.save(p);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("File upload operation is performed in Product Management");
		return ResponseEntity.ok(new MessageResponse("File Uploaded Successfully!"));
	}

	@Override
	public GenericResponse findByNameContaining(String Name) {
		logger.info("Find by Name containing operation is performed in Product Management");
		List<Product> product = productRepository.findByNameContaining(Name);
		if (product.isEmpty()) {
			throw new ResourceNotFoundException("product is not found by this name");
		}
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setData(product);
		genericResponse.setDescription("Product Management");
		genericResponse.setMessage("Products");
		genericResponse.setStatus("Active");
		return genericResponse;
	}

	@Override
	public ResponseEntity<?> saveProducts(List<Product> product) {
		// LocalDateTime now = LocalDateTime.now();
		int size = product.size();
		int counter = 0;
		List<Product> temp = new ArrayList<>();
		for (Product pro : product) {
//			menu.setTable_order(tableOrder+counter);
//			menu.setCreated_at(now);
//			menu.setUpdated_at(now);
			pro.setStatus(AppConstant.ActiveStatus);
			temp.add(pro);
			if ((counter + 1) == size) {
				productRepository.saveAll(temp);
				temp.clear();
			}
			counter++;
		}
		logger.info("Save list of product operation is performed in Product Management");
		return ResponseEntity.ok(new MessageResponse("Products Added Successfully!"));
	}

	@Override
	public ResponseEntity<Map<String, Object>> findByPageAndSize(int page, int size) {
		try {
			List<Product> product = new ArrayList<Product>();
			Pageable paging = PageRequest.of(page, size);

			Page<Product> pageTuts = productRepository.findAllByStatus(AppConstant.ActiveStatus, paging);
			product = pageTuts.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("data", product);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> findByPageAndSizeAndName(String title, int page, int size,
			String[] sort) {
		try {
			List<Order> orders = new ArrayList<Order>();

			if (sort[0].contains(",")) {
				// will sort more than 2 fields
				// sortOrder="field, direction"
				for (String sortOrder : sort) {
					String[] _sort = sortOrder.split(",");
					orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
				}
			} else {
				// sort=[field, direction]
				orders.add(new Order(getSortDirection(sort[1]), sort[0]));
			}

			List<Product> product = new ArrayList<Product>();
			Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

			Page<Product> pageTuts;
			if (title == null) {
				pageTuts = productRepository.findAll(pagingSort);
			} else {
				pageTuts = productRepository.findByNameContaining(title, pagingSort);
			}
			product = pageTuts.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("data", product);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}

		return Sort.Direction.ASC;
	}

	@Override
	public Product findProductById1(Long id) {
		Product product = productRepository.findByIdAndStatus(id, AppConstant.ActiveStatus);
		return product;
	}

	@Override
	public ByteArrayInputStream load() {
		List<Product> product = productRepository.findAllByStatus(AppConstant.ActiveStatus);
		ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(product);
		return in;
	}

	@Override
	public List<Product> getProductByOrder() {
		return productRepository.OrderByIdDesc();
	}

	@Override
	public File load1() {

		List<Product> product = productRepository.findAllByStatus(AppConstant.ActiveStatus);
		File in = ExcelHelper2.tutorialsToExcel(product);
		return in;
	}

}
