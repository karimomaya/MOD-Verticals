package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.TechnicalSupportReport;
import com.mod.rest.model.User;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.TechnicalSupportRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by omaradl on 5/21/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/technicalSupport")
public class TechnicalSupportController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    TechnicalSupportRepository technicalSupportRepository;

    @GetMapping("export/{reportType}/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("reportType") int reportType,
                                         @PathVariable("startDate") String startDateString,
                                         @PathVariable("endDate") String endDateString) {
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
            if (reportType == 1) {
                List<TechnicalSupportReport> technicalSupportReports = technicalSupportRepository.getTechnicalSupportStatistics(startDate,endDate);
                file = excelWriterService.generate(technicalSupportReports,"TechnicalSupport");
            } else if (reportType == 2) {
                List<TechnicalSupportReport> technicalSupportReports = technicalSupportRepository.getTechnicalSupportStatistics(startDate,endDate);
                file = excelWriterService.generate(technicalSupportReports);
            }

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
