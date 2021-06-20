package com.elkattanman.javafxapp.services;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportService <L extends Collection>{

//    @Autowired

    public String exportReport(String report, String key, Object value, L list) throws IOException, JRException {
        /* Convert List to JRBeanCollectionDataSource */
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        /* Map to hold Jasper report Parameters */
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(key, value);

        //read jrxml file and creating jasperdesign object
//        File file = ResourceUtils.getFile("classpath:templates/buyReceipt.jrxml");
        Resource sourceFile = new ClassPathResource("templates/buyReceipt.jrxml");
        InputStream input =sourceFile.getInputStream();

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        /*compiling jrxml with help of JasperReport class*/
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        /* Using jasperReport object to generate PDF */
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        /*call jasper engine to display report in jasperviewer window*/
        JasperViewer.viewReport(jasperPrint, false);

//        if (reportFormat.equalsIgnoreCase("html")) {
//            JasperExportManager.exportReportToHtmlFile(jasperPrint, File.pathSeparator + "report-html.html");
//        }
//        if (reportFormat.equalsIgnoreCase("pdf")) {
//            JasperExportManager.exportReportToPdfFile(jasperPrint, File.pathSeparator + "report-html.pdf");
//        }
        return "report generated in path : ";
    }

}