package com.mod.rest.controller;

import com.mod.rest.model.AttendanceReport;
import com.mod.rest.repository.AttendanceReportRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.LookupService;
import com.mod.rest.service.SessionService;
import com.mod.rest.service.UnitService;
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
 * Created by MinaSamir on 6/16/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/attendanceReport")
public class AttendanceReportController {
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
    AttendanceReportRepository attendanceReportRepository;

    @GetMapping("export/{lang}/{date}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("lang") String lang,
                                         @PathVariable("date") String date) {

        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;

        //http://localhost:8081/api/attendanceReport/export/ar/2020-06-15
        List<AttendanceReport> attendanceReports = attendanceReportRepository.getAttendanceReportByDate(date);
        System.out.println(attendanceReports);
        unitService.substituteUnitCodes(attendanceReports, "directorate", lang);
        file = excelWriterService.generate(attendanceReports);

        if (file == null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"null\"").body(null);
        }
        respHeaders.setContentLength(file.length());
        try {
            bytes = Files.readAllBytes(file.toPath());

            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"null\"").body(null); // used to download file
    }
}
