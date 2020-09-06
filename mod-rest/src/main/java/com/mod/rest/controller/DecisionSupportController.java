package com.mod.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.ReportObject;
import com.mod.rest.model.Task;
import com.mod.rest.model.TaskDecisionSupport;
import com.mod.rest.repository.TaskDecisionSupportRepository;
import com.mod.rest.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/decision-support")
public class DecisionSupportController {

    @Autowired
    TaskDecisionSupportRepository taskDecisionSupportRepository;

    @Autowired
    PDFService pdfService;

    @GetMapping("export/pdf/{entityItemId}/{owner}")
    @ResponseBody
    public ResponseEntity<byte[]> generateTaskReportPDF(@PathVariable("entityItemId") String entityItemId, @PathVariable("owner") String owner){
        try {

//            MOD_TM_SP_task_GetCreatedTaskByEntityItemId
            HttpHeaders respHeaders = new HttpHeaders();

            List<TaskDecisionSupport> taskList = taskDecisionSupportRepository.getCreatedTaskByEntityItemId(1, Integer.MAX_VALUE, "startDate", "sortAsc", owner,  entityItemId );

            if(taskList.size() == 0){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null); // used to download file
            }

            String templateName = "pdf-template/Decision-support-report-template"; // pdfService.getTemplateName(taskList.get(0));
            System.out.println("get template name: " + templateName);

            File file = pdfService.generate(taskList,  templateName + ".html", "task-data");

            byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
            respHeaders.setContentLength(bytes.length);
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf");

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }

}
