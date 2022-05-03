package com.ecommerce.training.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ecommerce.training.models.Product;



public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Name", "Description", "Part Number","Picture","Gst","Price","Unit" };
	  static String SHEET = "Products";

	  public static ByteArrayInputStream tutorialsToExcel(List<Product> pro) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);
	      //sheet.protectSheet("TestPassword"); //this line for only protected the cell.thats means do not modify the cell

	      // Header
	     
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (Product product : pro) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(product.getId());
	        row.createCell(1).setCellValue(product.getName());
	        row.createCell(2).setCellValue(product.getDescription());
	        row.createCell(3).setCellValue(product.getPartnumber());
	        row.createCell(4).setCellValue(product.getPicture());
	        row.createCell(5).setCellValue(product.getGst());
	        row.createCell(6).setCellValue(product.getPrice());
	        row.createCell(7).setCellValue(product.getUnit());
	      }
	      workbook.write(out);
	   
	      
	      
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	    
	    	  
	  }
}
