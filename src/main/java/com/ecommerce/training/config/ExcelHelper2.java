package com.ecommerce.training.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ecommerce.training.models.Product;



public class ExcelHelper2 {
	  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Name", "Description", "Part Number","Picture","Gst","Price","Unit" };
	  static String SHEET = "Products";

	  public static File tutorialsToExcel(List<Product> pro) {

	    try (XSSFWorkbook workbook = new XSSFWorkbook();) {
	    	 String pass="test";
		      
			  workbook.setWorkbookPassword(pass, HashAlgorithm.sha384);
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
	      for(int i = 0; i < HEADERs.length; i++) {
	          sheet.autoSizeColumn(i);
	      }
	      File file = new File("product.xlsx");
	      FileOutputStream fileOut = new FileOutputStream(file);
	      workbook.write(fileOut);
	      fileOut.close();

	      // Closing the workbook
	      workbook.close();
	      try (POIFSFileSystem fs = new POIFSFileSystem()) {
	          EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
	          // EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile,
	          // CipherAlgorithm.aes192, HashAlgorithm.sha384, -1, -1, null);
	          Encryptor enc = info.getEncryptor();
	          enc.confirmPassword(pass);
	          // Read in an existing OOXML file and write to encrypted output stream
	          // don't forget to close the output stream otherwise the padding bytes aren't
	          // added
	          try (OPCPackage opc = OPCPackage.open(file, PackageAccess.READ_WRITE);
	                  OutputStream os = enc.getDataStream(fs)) {
	              opc.save(os);
	          } catch (InvalidFormatException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          } catch (IOException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          } catch (GeneralSecurityException e1) {
	              // TODO Auto-generated catch block
	              e1.printStackTrace();
	          }
	          // Write out the encrypted version
	          try (FileOutputStream fos = new FileOutputStream(file)) {
	              fs.writeFilesystem(fos);
	          } catch (FileNotFoundException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          } catch (IOException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          }
	      } catch (IOException e2) {
	          // TODO Auto-generated catch block
	          e2.printStackTrace();
	      }
	      System.out.println("Excel file exported");

	      return file;
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
