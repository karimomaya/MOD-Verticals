package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.*;
import com.mod.rest.repository.EntityRepository;
import com.mod.rest.repository.IndividualRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.SessionService;
import lombok.extern.slf4j.Slf4j;
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
 * Created by omaradl on 5/21/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/contactTracker")
@Slf4j
public class ContactTrackerController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    Environment env;
    @Autowired
    EntityRepository entityRepository;
    @Autowired
    IndividualRepository individualRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("export/{report}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("report") String reportStr){
        log.info("Export function executed using reportStr: " + reportStr);
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        String fname = env.getProperty("contact-tracker-name");

        byte[] bytes = null;
        try {
            ReportObject reportObject = mapper.readValue(reportStr, ReportObject.class);
            reportObject = reportObject.build();
            if (reportObject.getReportType() == 1 ) {
                log.info("Report Type Entities");
                List<EntityReport> entityReports = entityRepository.getEntitiesByType(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityType(),reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getTags());
                log.info("Number of element founds: " + entityReports.size());
                file = excelWriterService.generate(entityReports, "Contact Tracker Entities");
            } else if (reportObject.getReportType() == 2 ) {
                log.info("Report Type Private Entities");
                List<EntityReport> entityReports = entityRepository.getPrivateEntities(1, Integer.MAX_VALUE, "", "" ,reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getIsRegistered(),reportObject.getLicenseNumber(),reportObject.getSupplierStatus(),reportObject.getTags());
                log.info("Number of element founds: " + entityReports.size());
                file = excelWriterService.generate(entityReports, "Contact Tracker Entities");
            }else if (reportObject.getReportType() == 3 ) {
                log.info("Report Type Individuals");
                List<IndividualReport> individualReports = individualRepository.getIndividuals(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition(),reportObject.getTags());
                log.info("Number of element founds: " + individualReports.size());
                file = excelWriterService.generate(individualReports);
            }else if (reportObject.getReportType() == 4) {
                log.info("Report Type Ministry Users");
                List<User> ministryUsersReports = userRepository.getMinistryUsers(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition());
                log.info("Number of element founds: " + ministryUsersReports.size());
                file = excelWriterService.generate(ministryUsersReports);
            }

            if(file == null){
                log.warn("It seems file is Null");
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fname+".xlsx");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }
}
