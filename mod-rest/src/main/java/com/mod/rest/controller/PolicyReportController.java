package com.mod.rest.controller;

import com.mod.rest.model.PolicyReport;
import com.mod.rest.repository.PolicyReportRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karim on 6/23/20.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/policyReport")
public class PolicyReportController {
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    PolicyReportRepository policyReporttRepository;
    @Autowired
    PDFService pdfService;
    @Autowired
    Environment env;

    @GetMapping("exportPDF/{startDate}/{endDate}/{userEntityId}/{userUnitId}/{pageNumber}/{pageSize}")
    @ResponseBody
    public ResponseEntity<byte[]> generatePolicyPDF(
                                         @PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate,
                                         @PathVariable("userEntityId") int userEntityId,
                                         @PathVariable("userUnitId") String userUnitId,
                                         @PathVariable("pageNumber") int pageNumber,
                                         @PathVariable("pageSize") int pageSize) {
       Date sDate=Utils.convertStringToDate(startDate);
       Date eDate=Utils.convertStringToDate(endDate);
        HttpHeaders respHeaders = new HttpHeaders();
        ArrayList<PolicyReport> policies =policyReporttRepository.getPolicyReportStatistics("",sDate,eDate,pageNumber, pageSize,userEntityId,userUnitId );
        String templateName = "";
        String fname = env.getProperty("policy-management-name");
        if(policies.size() > 0) templateName = pdfService.getTemplateName(policies.get(0));
        System.out.println("get template name: " + templateName);
        try {
            File file = pdfService.generate(policies, templateName + ".html", "policy-report");

            byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
            respHeaders.setContentLength(bytes.length);
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fname+".pdf");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null); // used to download file

    }

    @GetMapping("exportExcel/{startDate}/{endDate}/{userEntityId}/{userUnitId}")
    @ResponseBody
    public ResponseEntity<byte[]> generatePolicyExcel(

                                         @PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate,
                                         @PathVariable("userEntityId") int userEntityId,
                                         @PathVariable("userUnitId") String userUnitId) {
        HttpHeaders respHeaders = new HttpHeaders();
        Date sDate=Utils.convertStringToDate(startDate);
        Date eDate=Utils.convertStringToDate(endDate);
        String fname = env.getProperty("policy-management-name");
        File file = null;
        byte[] bytes = null;
        try{

            ArrayList<PolicyReport> policies = policyReporttRepository.getPolicyReportStatistics("",sDate,eDate,1, Integer.MAX_VALUE,userEntityId,userUnitId );
            file = excelWriterService.generate(policies,"PolicyReport");

            if (file == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fname + ".xlsx");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null); // used to download file
    }
}
