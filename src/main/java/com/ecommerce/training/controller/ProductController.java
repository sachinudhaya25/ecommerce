package com.ecommerce.training.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.ProductDto;
import com.ecommerce.training.models.Product;
import com.ecommerce.training.repository.ProductRepository;
import com.ecommerce.training.service.ProductService;
import com.ecommerce.training.service.ProductServiceImpl;
import com.ecommerce.training.utility.AppConstant;







@RestController
@RequestMapping("/api/product")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	ProductService productService;
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	@Autowired
	ProductRepository productRepository;
	@GetMapping("/get")
	public GenericResponse getAllProduct(@RequestHeader("Authorization") String authorization)
	{
		logger.info("Get all products method start....");
		return productService.getAllProducts();
	}
	@GetMapping("/get/{id}")
	public GenericResponse getProductById(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
	{
		logger.info("Get product by id method start....");
		return productService.findProductById(id);
	}
	@GetMapping("/get/name")
	public GenericResponse getProductByName(@RequestHeader("Authorization") String authorization,@RequestParam(required = false, defaultValue = "") String name)
	{
		logger.info("Get product by name method start....");
		return productService.findByName(name);
	}
	@GetMapping("/get/namestart")
	public GenericResponse getProductByNameStart(@RequestHeader("Authorization") String authorization,@RequestParam(required = false, defaultValue = "") String name)
	{
		logger.info("Get product by name search method start....");
		return productService.findByNameStartingWith(name);
	}
	@GetMapping("/get/namecontaining")
	public GenericResponse getProductByNameContaining(@RequestHeader("Authorization") String authorization,@RequestParam(required = false, defaultValue = "") String name)
	{
		logger.info("Get product by name containing method start....");
		return productService.findByNameContaining(name);
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveProduct(@Valid @RequestHeader("Authorization") String authorization, @RequestBody Product product)
	{
		logger.info("Sqave product method start....");
		return productService.saveProduct(product);
	}
	@PostMapping("/saveAll")
	public ResponseEntity<?> saveProducts(@RequestBody List<Product> product)
	{
		logger.info("Sqave products method start....");
		return productService.saveProducts(product);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String authorization,@RequestBody Product product,@PathVariable Long id)
	{
		logger.info("Update product method start....");
		return productService.updateProduct(product, id);
	}
	@PostMapping("/uploadfile")
	 public ResponseEntity<?> mapReapExcelDatatoDB(@RequestHeader("Authorization") String authorization,@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException 
	{
		logger.info("Upload file product method start....");
	        return productService.uploadFile(reapExcelDataFile);       
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
	{
		logger.info("Delete product method start....");
		return productService.deleteProduct(id);
	}
	@GetMapping("/pageAndSize")
	public ResponseEntity<Map<String, Object>> findByPageAndSize(
			@RequestHeader("Authorization") String authorization,
	      @RequestParam(defaultValue = "0") int page,
	      @RequestParam(defaultValue = "5") int size) {
		logger.info("Find products by page and size method start....");
	    return productService.findByPageAndSize(page, size);
	}
	@GetMapping("/pageAndSizeAndName")
	public ResponseEntity<Map<String, Object>> findByPageAndSizeAndName(
			@RequestHeader("Authorization") String authorization,
	      @RequestParam(required = false) String title,
	      @RequestParam(defaultValue = "0") int page,
	      @RequestParam(defaultValue = "3") int size,
	      @RequestParam(defaultValue = "id,desc") String[] sort) {
		logger.info("Find products by page and size ans name method start....");
		return productService.findByPageAndSizeAndName(title, page, size, sort);
	 }
	@GetMapping("/download")
	  public ResponseEntity<Resource> getFile() throws IOException, GeneralSecurityException {
	    String filename = "product.xlsx";
	    InputStreamResource file = new InputStreamResource(productService.load()); 
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }
	@GetMapping("/getbyorder")
	public List<Product> getAllProductByOrder(@RequestHeader("Authorization") String authorization)
	{
		logger.info("Get all products method start....");
		return productService.getProductByOrder();
	}
	
	  @GetMapping("/downloadfilewithpassword")
	  public ResponseEntity<?> getFileWithPassword() throws IOException, GeneralSecurityException {
		
		//note:the code was sucess but file is not open in the system try it on the  another system or mobile
	    String filename = "product.xlsx";
		var response = productService.load1();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(response));
		String contentType = "application/octet-stream";
		return ResponseEntity.status((response == null) ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getName() + "\"")
				.body(resource);
	  }
	  @GetMapping("/downloadfilewithpassword1")
	  public File getFileWithPassword1() throws IOException, GeneralSecurityException {
		return productService.load1();
		
	  }
	
	
	
}
