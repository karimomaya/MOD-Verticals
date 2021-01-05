package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mod.rest.model.CandidateDataReport;
import com.mod.rest.repository.CandidateDataReportRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by aly.aboulgheit on 5/28/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/candidateDataReport")
public class CandidateDataReportController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    CandidateDataReportRepository candiateDataRepository;
    @Autowired
    Environment env;


    @GetMapping("export/{startDateString}/{endDateString}")
    @ResponseBody
    public ResponseEntity<byte[]> export(
                                         @PathVariable("startDateString") String startDate,
                                         @PathVariable("endDateString") String endDate) {

        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try{
            List<CandidateDataReport> candidateDateReports = candiateDataRepository.getCandidateDataReport(startDate, endDate);
            file = excelWriterService.generate(candidateDateReports);

            if (file == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }

            String fname = env.getProperty("candidate-range-name");
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fname +".xlsx");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        } catch ( IOException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null); // used to download file
    }
}
