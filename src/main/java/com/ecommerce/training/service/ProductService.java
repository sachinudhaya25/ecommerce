package com.ecommerce.training.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.dto.ProductDto;
import com.ecommerce.training.models.Product;









public interface ProductService {

	    ResponseEntity<?> saveProduct(Product product);
	    void saveProduct1(Product product);
	    ResponseEntity<?> saveProducts(List<Product> product);
	    GenericResponse getAllProducts();
		GenericResponse findProductById(Long id);
		ResponseEntity<?> updateProduct(Product product,Long id);
		ResponseEntity<?>  deleteProduct(Long id);
		GenericResponse findByName(String Name);
		GenericResponse findByNameStartingWith(String Name);
		GenericResponse findByNameContaining(String Name);
		ResponseEntity<?> uploadFile(MultipartFile reapExcelDataFile) throws IOException;
		ResponseEntity<Map<String, Object>> findByPageAndSize(int page,int size);
		ResponseEntity<Map<String, Object>> findByPageAndSizeAndName(String title,int page,int size,String[] sort);
		
		Product findProductById1(Long id);
		public ByteArrayInputStream load();
	
		List<Product> getProductByOrder();
		
		
		public File load1();
		
		
	
}
