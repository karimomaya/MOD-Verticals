package com.mod.rest.controller;

import com.mod.rest.model.ProofreadingReportMonthly;
import com.mod.rest.repository.ProofreadingReportMonthlyRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.LookupService;
import com.mod.rest.service.SessionService;
import com.mod.rest.service.UnitService;
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
 * Created by MinaSamir on 5/27/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/proofreadingReportMonthly")
public class ProofreadingReportMonthlyController {
    @Autowired
    Environment env;

    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;

//    @Autowired
//    LookupService lookupService;
    @Autowired
    UnitService unitService;
//    @Autowired
//    RoleService roleService;

    @Autowired
    ProofreadingReportMonthlyRepository proofreadingReportMonthlyRepository;

    @GetMapping("export/{lang}/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("lang") String lang,
                                         @PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate) {

        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;

        //http://localhost:8081/api/proofreadingReportMonthly/export/ar/2019-05-28/2020-05-26
        List<ProofreadingReportMonthly> proofreadingReportsMonthly = proofreadingReportMonthlyRepository.getProofreadingReportMonthly(startDate, endDate);
//        System.out.println(proofreadingReportsMonthly);
        unitService.substituteUnitCodes(proofreadingReportsMonthly, "request_Department", lang);
//        System.out.println(lookupService.getLookupValuesByCategory("classification"));
//        lookupService.substituteLookupIds(proofreadingReportsMonthly, "classification", "degreeOfConfidentiality", lang);
//        lookupService.substituteLookupIds(proofreadingReportsMonthly, "priority", "degreeOfPrecedence", lang);
//        System.out.println(roleService.getRoleNamesMultilingual());
        file = excelWriterService.generate(proofreadingReportsMonthly);

        if (file == null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"null\"").body(null);
        }
        respHeaders.setContentLength(file.length());
        try {
            String fname = env.getProperty("proofreading-report-monthly-name");

            bytes = Files.readAllBytes(file.toPath());

            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fname +".xlsx");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"null\"").body(null); // used to download file
    }
}
