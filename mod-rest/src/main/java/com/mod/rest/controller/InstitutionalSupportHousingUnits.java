package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mod.rest.model.InstitutionalSupportHousingUnitEvacuationReport;
import com.mod.rest.model.InstitutionalSupportHousingUnitStatusReport;
import com.mod.rest.repository.InstitutionalSupportHousingUnitEvacuationReportRepository;
import com.mod.rest.repository.InstitutionalSupportHousingUnitStatusReportRepository;
import com.mod.rest.service.ExcelWriterService;
import lombok.Data;
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord;
import com.mod.rest.service.LookupService;

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
 * Created by abdallah.shaaban on 9/1/2020.
 */
@Data
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api/institutionalSupportHousingUnits")

public class InstitutionalSupportHousingUnits {
    @Autowired
    LookupService lookupService;
    @Autowired
    InstitutionalSupportHousingUnitStatusReportRepository institutionalSupportHousingUnitStatusReportRepository;
    @Autowired

    InstitutionalSupportHousingUnitEvacuationReportRepository institutionalSupportHousingUnitEvacuationReportRepository;
    @Autowired
    ExcelWriterService excelWriterService;

    @GetMapping("export/housingUnitsStatusReport/{houseUnitStatus}/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity<byte[]> exportStatusReport(@PathVariable("houseUnitStatus") String houseUnitStatus,
                                                     @PathVariable("startDate") String startDateStr,
                                                     @PathVariable("endDate") String endDateStr) throws ParseException {

        if (houseUnitStatus.equals("null")) houseUnitStatus = "";
        if (startDateStr.equals("null"))
            startDateStr = null;

        if (endDateStr.equals("null"))
            endDateStr = null;


        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try {
            List<InstitutionalSupportHousingUnitStatusReport> HousingUnitStatusReport = institutionalSupportHousingUnitStatusReportRepository.getInstitutionalSupportHousingUnitStatusReport
                    (startDateStr, endDateStr, houseUnitStatus, 0, Integer.MAX_VALUE, "");
            lookupService.substituteLookupIds(HousingUnitStatusReport, "housingUnitType", "flatType", "ar");
            lookupService.substituteLookupIds(HousingUnitStatusReport, "housingUnitStatus", "flatStatus", "ar");

            file = excelWriterService.generate(HousingUnitStatusReport);

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"null\"").body(null); // used to download file

    }

    @GetMapping("export/housingUnitsEvacuationReport/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity<byte[]> exportEvacuationReport(@PathVariable("startDate") String startDateStr,
                                                         @PathVariable("endDate") String endDateStr) throws ParseException {
        if (startDateStr.equals("null"))
            startDateStr = null;

        if (endDateStr.equals("null"))
            endDateStr = null;


        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try {
            List<InstitutionalSupportHousingUnitEvacuationReport> HousingUnitStatusReport = institutionalSupportHousingUnitEvacuationReportRepository.getInstitutionalSupportHousingUnitEvacuationReport
                    (startDateStr, endDateStr, 0, Integer.MAX_VALUE);
            lookupService.substituteLookupIds(HousingUnitStatusReport, "housingUnitType", "flatType", "ar");
//            lookupService.substituteLookupIds(HousingUnitStatusReport, "housingUnitStatus", "flatStatus", "ar");

            file = excelWriterService.generate(HousingUnitStatusReport);

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"null\"").body(null); // used to download file

    }

}
