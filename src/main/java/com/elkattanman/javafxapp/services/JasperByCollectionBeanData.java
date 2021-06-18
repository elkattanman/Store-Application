package com.elkattanman.javafxapp.services;

import com.elkattanman.javafxapp.domain.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class JasperByCollectionBeanData {

    public static void main(String[] args) throws JRException, FileNotFoundException {

        /* Output file location to create report in pdf form */
        String outputFile = "D:\\" + "JasperReportExample.pdf";

        /* List to hold Items */
        Set<ReceiptItem> listItems = new HashSet<>();

        ReceiptItem receiptItem = ReceiptItem.builder()
                .id(1)
                .product(Product.builder().id(1).name("product1").price(14).type("mobile").build())
                .quantity(5)
                .build();
        ReceiptItem receiptItem1 = ReceiptItem.builder()
                .id(1)
                .product(Product.builder().id(2).name("product2").price(25).type("mobile").build())
                .quantity(5)
                .build();
        ReceiptItem receiptItem2 = ReceiptItem.builder()
                .id(1)
                .product(Product.builder().id(3).name("product3").price(11).type("mobile").build())
                .quantity(5)
                .build();

        /* Add Items to List */
        listItems.add(receiptItem);
        listItems.add(receiptItem1);
        listItems.add(receiptItem2);

        ReceiptHeader receiptHeader=ReceiptHeader.builder()
                .id(1)
                .receiptItems(listItems)
                .totalPrice(15)
                .store(Store.builder().id(1).name("store1").address("address1").phone("00000ph").branch("branch").build())
                .remain(15.5)
                .paid(15)
                .supplier(Supplier.builder().id(1).company("company").Name("name").city("city").phone("0000").email("email").build())
                .build();

        /* Convert List to JRBeanCollectionDataSource */
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);

        /* Map to hold Jasper report Parameters */
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("receiptHeader", receiptHeader);

        //read jrxml file and creating jasperdesign object
        File file = ResourceUtils.getFile("classpath:templates/buyReceipt.jrxml");
        InputStream input = new FileInputStream(file);

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        /*compiling jrxml with help of JasperReport class*/
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        /* Using jasperReport object to generate PDF */
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, itemsJRBean);

        /*call jasper engine to display report in jasperviewer window*/
        JasperViewer.viewReport(jasperPrint);


        /* outputStream to create PDF */
        //OutputStream outputStream = new FileOutputStream(new File(outputFile));


        /* Write content to PDF file */
        //JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        System.out.println("File Generated");

    }

}


