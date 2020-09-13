package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mod.rest.model.CarGeneralReport;
import com.mod.rest.model.CarMaintenanceReport;
import com.mod.rest.model.CarSummary;
import com.mod.rest.model.ReceivedCarReport;
import com.mod.rest.repository.CarGeneralReportRepository;
import com.mod.rest.repository.CarMaintenanceRepository;
import com.mod.rest.repository.CarSummaryRepository;
import com.mod.rest.repository.ReceivedCarsReportRepository;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/carRecord")
public class CarRecordController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    CarGeneralReportRepository carGeneralReportRepository;
    @Autowired
    ReceivedCarsReportRepository receivedCarsReportRepository;
    @Autowired
    CarMaintenanceRepository carMaintenanceRepository;
    @Autowired
    CarSummaryRepository carSummaryRepository;


    @GetMapping("export-carRecord/{reportType}/{startDate}/{endDate}/{year}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate,
                                         @PathVariable("reportType") int reportType,
                                         @PathVariable("year") String year){
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try {
            if(reportType == 1){
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date start = format.parse(startDate);
                    Date end = format.parse(endDate);
                    List<CarGeneralReport> carGeneralReportList= carGeneralReportRepository.findByExpiryDateOfTheLicenseBetween(start,end);
//                    int currentCarsSize = carGeneralReportList.size();
//                    Iterator<CarGeneralReport> it = new Iterator<CarGeneralReport>() {
//                        private int currentIndex = 0;
//
//                        @Override
//                        public boolean hasNext() {
//                            return currentIndex < currentCarsSize && carGeneralReportList.get(currentIndex) != null;
//
//                        }
//
//                        @Override
//                        public CarGeneralReport next() {
//                            return carGeneralReportList.get(currentIndex++);
//                        }
//                    };
                    Iterator<CarGeneralReport> it2= carGeneralReportList.iterator();
                    while (it2.hasNext()){
                        CarGeneralReport currentObj = it2.next();
                        if(currentObj.getStatus().equals("مشطوبة")){
                            it2.remove();
                        }
                    }
//                    for (CarGeneralReport car :carGeneralReportList){
//                            if(car.getStatus().equals("مشطوبة")){
//                                carGeneralReportList.remove(car);
//                            }
//                    }
                    file = excelWriterService.generate(carGeneralReportList);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(reportType == 2){
                List<ReceivedCarReport> receivedCarReports = receivedCarsReportRepository.getReceivedCarsRecordBetweenReceiveDate(startDate,endDate);
                file = excelWriterService.generate(receivedCarReports);

            }
            if(reportType == 3) {
                List<CarMaintenanceReport> carMaintenanceReports = carMaintenanceRepository.getMaintenenceCountByYear(year);
                file = excelWriterService.generate(carMaintenanceReports);
            }
            if (reportType == 4){
                List<CarSummary> carSummaries = carSummaryRepository.getCarSummary();
                file = excelWriterService.generate(carSummaries);
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
