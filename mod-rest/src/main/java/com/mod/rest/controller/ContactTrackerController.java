package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.*;
import com.mod.rest.repository.EntityRepository;
import com.mod.rest.repository.IndividualRepository;
import com.mod.rest.repository.UserRepository;
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
import java.util.List;

/**
 * Created by omaradl on 5/21/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/contactTracker")
public class ContactTrackerController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    EntityRepository entityRepository;
    @Autowired
    IndividualRepository individualRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("export/{report}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("report") String reportStr){
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try {
            ReportObject reportObject = mapper.readValue(reportStr, ReportObject.class);
            reportObject = reportObject.build();
            if (reportObject.getReportType() == 1 ) {
                List<EntityReport> entityReports = entityRepository.getEntitiesByType(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityType(),reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getTags());
                file = excelWriterService.generate(entityReports, "Contact Tracker Entities");
            } else if (reportObject.getReportType() == 2 ) {
                List<EntityReport> entityReports = entityRepository.getPrivateEntities(1, Integer.MAX_VALUE, "", "" ,reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getIsRegistered(),reportObject.getLicenseNumber(),reportObject.getSupplierStatus(),reportObject.getTags());
                file = excelWriterService.generate(entityReports, "Contact Tracker Entities");
            }else if (reportObject.getReportType() == 3 ) {
                List<IndividualReport> individualReports = individualRepository.getIndividuals(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition(),reportObject.getTags());
                file = excelWriterService.generate(individualReports);
            }else if (reportObject.getReportType() == 4) {
                List<User> ministryUsersReports = userRepository.getMinistryUsers(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition());
                file = excelWriterService.generate(ministryUsersReports);
            }

            if(file == null){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }
}
