package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mod.rest.model.DiaryOfUnderstandingReport;
import com.mod.rest.repository.DiaryOfUnderstandingRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.LookupService;
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
 * Created by aly.aboulgheit on 5/28/2020.
 */


// localhost:8081/api/diaryOfUnderstandingReport/export/ar/2019-06-25/2020-06-25

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/diaryOfUnderstandingReport")
public class DiaryOfUnderstandingReportController {
    @Autowired
    SessionService sessionService;
    @Autowired
    LookupService lookupService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    DiaryOfUnderstandingRepository diaryOfUnderstandingRepository;

    @GetMapping("export/{lang}/{startDateString}/{endDateString}")
    @ResponseBody
    public ResponseEntity<byte[]> export(
                                        @PathVariable("lang") String lang,
                                         @PathVariable("startDateString") String startDate,
                                         @PathVariable("endDateString") String endDate) {

        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try{
            List<DiaryOfUnderstandingReport> diaryOfUnderstandingReports = diaryOfUnderstandingRepository.getDiaryOfUnderstandingReport(startDate, endDate);
            System.out.println(diaryOfUnderstandingReports);

            lookupService.substituteLookupIds(diaryOfUnderstandingReports, "diaryUnderstandingPlace", "placeOfDiary", lang);
            lookupService.substituteLookupIds(diaryOfUnderstandingReports, "entityType", "sectorType", lang);

            file = excelWriterService.generate(diaryOfUnderstandingReports);

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
        } catch (JsonProcessingException e){
            e.printStackTrace();
        } catch ( IOException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null); // used to download file
    }
}
