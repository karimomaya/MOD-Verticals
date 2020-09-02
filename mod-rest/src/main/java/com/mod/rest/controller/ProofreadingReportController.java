package com.mod.rest.controller;

import com.mod.rest.model.ProofreadingReport;
import com.mod.rest.repository.ProofreadingReportRepository;
import com.mod.rest.repository.UnitRepository;
import com.mod.rest.service.*;
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
 * Created by MinaSamir on 5/26/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/proofreadingReport")
public class ProofreadingReportController {
    @Autowired
    Environment env;

    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;

    @Autowired
    LookupService lookupService;
    @Autowired
    UnitService unitService;
//    @Autowired
//    RoleService roleService;

    @Autowired
    ProofreadingReportRepository proofreadingReportRepository;

    @GetMapping("export/{lang}/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("lang") String lang,
                                         @PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate) {

        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;

        //http://localhost:8081/api/proofreadingReport/export/ar/2019-05-28/2020-05-26
        List<ProofreadingReport> proofreadingReports = proofreadingReportRepository.getProofreadingReport(startDate, endDate);
//        System.out.println(proofreadingReports);
        unitService.substituteUnitCodes(proofreadingReports, "requestDepartment", lang);
//        System.out.println(lookupService.getLookupValuesByCategory("classification"));
        lookupService.substituteLookupIds(proofreadingReports, "classification", "degreeOfConfidentiality", lang);
        lookupService.substituteLookupIds(proofreadingReports, "priority", "degreeOfPrecedence", lang);
        lookupService.substituteLookupIds(proofreadingReports, "evaluation", "averageRating", lang);
//        System.out.println(roleService.getRoleNamesMultilingual());
        file = excelWriterService.generate(proofreadingReports);

        if (file == null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"null\"").body(null);
        }
        respHeaders.setContentLength(file.length());
        try {
            String fname = env.getProperty("proofreading-report-name");

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
