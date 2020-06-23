package com.mod.rest.controller;

import com.mod.rest.model.PolicyReport;
import com.mod.rest.repository.PolicyReportRepository;
import com.mod.rest.service.ExcelWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
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

    @GetMapping("export/{policyName}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("policyName") String policyName) {
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try{
            List<PolicyReport> policies = policyReporttRepository.getPolicyReportStatistics(policyName,1, Integer.MAX_VALUE);
            file = excelWriterService.generate(policies,"TechnicalSupport");


            if (file == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null); // used to download file
    }
}
