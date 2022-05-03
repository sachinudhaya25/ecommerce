package com.ecommerce.training.config;

import java.util.List;

import com.ecommerce.training.models.Bill;
import com.ecommerce.training.models.DeliveredProduct;

import java.awt.Color;
import java.io.IOException;

 
import javax.servlet.http.HttpServletResponse;
 
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;


public class BillPDFExporter {

	private Bill billData;
    
    public BillPDFExporter(Bill billData) {
        this.billData = billData;
    }
 
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        
        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Tax", font));
        table.addCell(cell);
             
    }
     
    private void writeTableData(PdfPTable table) {
        for (DeliveredProduct product : billData.getDeliveredProduct()) {
            table.addCell(product.getProduct().getName());
            table.addCell(product.getProduct().getDescription());
            table.addCell(String.valueOf(product.getDeliveredQty()));
            table.addCell(String.valueOf(product.getProduct().getPrice()));
            table.addCell(String.valueOf(product.getProduct().getGst())+"%");
        }
    }
     
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font font1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font font2= FontFactory.getFont(FontFactory.COURIER);
        Font font3= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(16);
        font1.setSize(18);
        font2.setSize(10);
        font3.setSize(10);
        font.setColor(Color.BLUE);
        font1.setColor(Color.BLACK);
        font2.setColor(Color.BLACK);
        font3.setColor(Color.BLACK);
        Paragraph p1 = new Paragraph("Eshop", font1);
        Paragraph p2 = new Paragraph("Invoice", font);
        Paragraph p4 = new Paragraph(
        		            "INVOICE NUMBER  :  "+billData.getBillNo()+"\n"+
                            "DATE OF ISSUE   :  "+billData.getBillDate()+"\n"+
        		            "Order Number    :  "+billData.getOrders().getOrderNumber(), font2);
        Paragraph p5 = new Paragraph("Billed To", font3);
        Paragraph p6 = new Paragraph(
        		 billData.getOrders().getCustomer().getName()+",\n"+
	             billData.getOrders().getCustomer().getAddress()+"\n", font2);
      
        p1.setAlignment(Paragraph.ALIGN_LEFT);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        p4.setAlignment(Paragraph.ALIGN_LEFT);
        p5.setAlignment(Paragraph.ALIGN_LEFT);
        p6.setAlignment(Paragraph.ALIGN_LEFT);
        
        Image image = Image.getInstance("D:\\private\\sachin\\java\\java_training\\e-commerce\\src\\main\\resources\\image\\eshop1.png");
        image.setAlignment(Image.RIGHT);
        document.add(p1);
        document.add(image);
        document.add(p5);
        document.add(p6);
        document.add(p2);
        document.add(p4);
         
        
       
       
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 3.5f, 1.5f, 3.0f,1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
        
        Paragraph p3 = new Paragraph("              "+"\n"+
        		 "SUBTOTAL              "+billData.getBillAmount()+"\n"+
				 "TAX               "+billData.getTax()+"\n"+
				 "--------------"+"\n"+
				 "              "+"\n"+
				 "INVOICE TOTAL              "+billData.getTotalBillAmount(), font2);
        p3.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p3);
         
        document.close();
         
    }
	
}
