package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.GraphDataHelper;
import com.mod.rest.model.ReportObject;
import com.mod.rest.model.Task;
import com.mod.rest.service.ReportService;
import com.mod.rest.system.Pagination;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
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
 * Created by karim.omaya on 12/27/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/report")
public class reportController {

    @Autowired
    ReportService reportService;

    @GetMapping("export/{report}/{samlart}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("samlart") String SAMLart,
                                       @PathVariable("report") String reportStr){


        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] isr = null;
        try {
            ReportObject reportObject = mapper.readValue(reportStr, ReportObject.class);
            reportObject = reportObject.build();
            reportObject = reportService.buildReportObject(reportObject.setSAMLart(SAMLart));

            reportObject = reportObject.changeDetectedReportType(3);
            file = reportService.execute(reportObject);
            respHeaders.setContentLength(file.length());
             isr = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file

    }

    @GetMapping("get/{report}")
    public ResponseBuilder<ObjectNode> report(@RequestHeader("samlart") String SAMLart,
                                              @PathVariable("report") String reportStr){
        ResponseBuilder<ObjectNode> responseBuilder = new ResponseBuilder<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result= mapper.createObjectNode();
        try {

            ReportObject reportObject = mapper.readValue(reportStr, ReportObject.class);
            reportObject = reportObject.build();

            reportObject = reportService.buildReportObject(reportObject.setSAMLart(SAMLart));

            if (reportObject.getDetectedReportType() == 0){ // generate graph
                List<GraphDataHelper> graphDataHelpers = reportService.execute(reportObject);
                List<Task> tasks = reportService.execute(reportObject.changeDetectedReportType(1));
                Long count = reportService.execute(reportObject.changeDetectedReportType(2));
                Pagination pagination = Utils.generatePagination(0, reportObject.getPageSize(), count);
                responseBuilder.setPagination(pagination);
                result.put("graph", Utils.writeObjectIntoString(graphDataHelpers));
                result.put("tasks", Utils.writeObjectIntoString(tasks));

                responseBuilder.status(ResponseCode.SUCCESS);

            } else if (reportObject.getDetectedReportType() == 1){ // generate table
                List<Task> tasks = reportService.execute(reportObject);

                if(tasks.size()>0){

                    Long count = reportService.execute(reportObject.changeDetectedReportType(2));
                    Pagination pagination =  Utils.generatePagination(reportObject.getPageNumber(), reportObject.getPageSize(), count);
                    responseBuilder.setPagination(pagination);
                    responseBuilder.status(ResponseCode.SUCCESS);
                    result.put("tasks", Utils.writeObjectIntoString(tasks));
                }
                else {
                    responseBuilder.status(ResponseCode.NO_CONTENT);
                }

                return responseBuilder.data(result).build();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return responseBuilder.data(result).build();

    }
}
