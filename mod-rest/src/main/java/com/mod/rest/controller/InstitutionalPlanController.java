package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.mod.rest.model.IPStrategicGoal;
import com.mod.rest.model.IPSubActivity;
import com.mod.rest.model.IPMainActivity;
import com.mod.rest.model.Unit;
import com.mod.rest.repository.IPMainActivityRepository;
import com.mod.rest.repository.UnitRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.repository.IPStrategicGoalRepository;
import com.mod.rest.repository.IPSubActivityRepository;

import com.mod.rest.service.PDFService;

import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.Utils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by abdallah.shaaban on 7/16/2020.
 */

@Data
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/institutionalPlan")
public class InstitutionalPlanController {

    @Autowired
    IPSubActivityRepository ipSubActivityRepository;
    @Autowired
    IPMainActivityRepository ipMainActivityRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    IPStrategicGoalRepository ipStrategicGoalRepository;
    @Autowired
    PDFService pdfService;
    @Autowired
    ExcelWriterService excelWriterService;

  @GetMapping("/IPStrategicGoal/export/Excel/{roadMapId}/{unitCode}/{halfOfYear}/{year}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable("roadMapId") String roadMapId,
                                         @PathVariable("unitCode") String unitCode,@PathVariable("halfOfYear") String halfOfYear,@PathVariable("year") String year) {

        List<IPStrategicGoal> strategicGoals = ipStrategicGoalRepository.getIPStrategicc(roadMapId, unitCode, halfOfYear, year);

            HttpHeaders respHeaders = new HttpHeaders();
            File file = null;
            byte[] bytes = null;

            try {

                file = excelWriterService.generate(strategicGoals);

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

    @GetMapping("/IPStrategicGoal/export/PDF/{roadMapId}/{unitCode}/{halfOfYear}/{year}")
    public ResponseEntity<byte[]> exportToPDF(@PathVariable("roadMapId") String roadMapId,
                                         @PathVariable("unitCode") String unitCode,@PathVariable("halfOfYear") String halfOfYear,@PathVariable("year") String year) {

        List<IPStrategicGoal> strategicGoals = ipStrategicGoalRepository.getIPStrategicc(roadMapId, unitCode, halfOfYear, year);
        byte[] bytes = null;
        try {
            File file = pdfService.generate(strategicGoals, "pdf-template/strategic-goal.html", "strategic-goal");
//            file = pdfService.generate(strategicGoals2, file.toURI().getPath(), "strategic-goal2");
            bytes = pdfService.generatePDF(file.getAbsolutePath());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attachment.pdf\"")
                    .body(bytes);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attachment.pdf\"")
                .body(bytes);

    }

    @GetMapping("/SubActivity/TimeLine/{unitTypeCode}/{unitCode}/{institutionalPlan}/{startDate}/{endDate}")
    public ResponseBuilder<String> subActivityTimeLine (@PathVariable("unitCode") String unitCode,@PathVariable("unitTypeCode") String unitTypeCode
                                                            ,@PathVariable("institutionalPlan") String institutionalPlan,
                                                            @PathVariable("startDate") String startDateStr
                                                            ,@PathVariable("endDate") String endDateStr){

//        String startDateStr = "2020-12-30";
//        String endDateStr = "2020-12-30";
        if(startDateStr =="null")startDateStr="";
        if(endDateStr =="null")endDateStr="";

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();

        JSONArray result = null;
        try {
            if(unitTypeCode.equals( "SEC")){
                JSONObject object = new JSONObject();
                object = getSubActivitiesBySECUnitCode(unitCode,institutionalPlan, null,startDateStr,endDateStr);

                result.put(object);
            }else if (unitTypeCode.equals("DIV")){
                result = getSubActivitiesByDIVUnitCode(unitCode,institutionalPlan,startDateStr,endDateStr);
            }else if (unitTypeCode.equals("DIR")){
                result = getSubActivitiesByDIRUnitCode(unitCode,institutionalPlan,startDateStr,endDateStr);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseBuilder.data(result.toString()).build();
    }

    private JSONObject getSubActivitiesBySECUnitCode(String unitCode, String institutionalPlan , Long mainActivityId, String startDateStr , String endDateStr) throws ParseException {

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
        JSONObject object = new JSONObject();
        JSONArray result = new JSONArray();
        List<IPSubActivity> subActivitiesList  = ipSubActivityRepository.getIPSubActivity(unitCode,institutionalPlan,mainActivityId, startDate,endDate);
        JSONArray data = new JSONArray();
        boolean subActivitiesFlag = false;
        for (IPSubActivity subActivity : subActivitiesList){
            subActivitiesFlag = true;
            JSONObject json = new JSONObject();
            // X Axis "Activity Name"
            json.put("x", subActivity.getSubActivityName());
            json.put("type", "subActivity");
            json.put("unitName_ar",subActivity.getUnitName_ar());
            json.put("unitName_en",subActivity.getUnitName_en());

            // Y Axis "Dates"
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(subActivity.getEndDate().getTime());
            jsonArray.put(subActivity.getStartDate().getTime());
            json.put("y", jsonArray);
            String color = detectTaskColor(subActivity.getProgress(),subActivity.getStartDate(),subActivity.getEndDate());
            json.put("color", color);
            json.put("tooltip", subActivity.getProgress());
            data.put(json);

        }
        if(subActivitiesFlag)
        object.put("data", data);
//        result.put(object);

        return object;
    }
    private JSONObject resolveMainActivityToJSON(IPMainActivity mainActivity){

        JSONObject object = new JSONObject();

        JSONArray data = new JSONArray();


            JSONObject json = new JSONObject();
            // X Axis "Activity Name"
            json.put("x", mainActivity.getActivityName());
            json.put("type", "mainActivity");

            json.put("unitName_ar",mainActivity.getUnitName_ar());
            json.put("unitName_en",mainActivity.getUnitName_en());

            // Y Axis "Dates"
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(mainActivity.getEndDate().getTime());
            jsonArray.put(mainActivity.getStartDate().getTime());
            json.put("y", jsonArray);
            Integer progress= Integer.parseInt(mainActivity.getProgress());
            String color = detectTaskColor(progress,mainActivity.getStartDate(),mainActivity.getEndDate());
            json.put("color", color);
            json.put("tooltip", mainActivity.getProgress());
            data.put(json);

        object.put("data", data);

        return object;
    }
    private JSONArray getSubActivitiesByDIVUnitCode(String unitCode,String institutionalPlan , String startDateStr , String endDateStr) throws ParseException {
        JSONArray result = new JSONArray();

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);

        List<IPMainActivity> mainActivitiesList = ipMainActivityRepository.getIPMainActitvityByDIVUnitCode(unitCode,institutionalPlan, startDate,endDate);

        for (IPMainActivity mainActivity : mainActivitiesList) {
            JSONObject object = new JSONObject();


            object = resolveMainActivityToJSON(mainActivity);
            result.put(object);
            Long id = mainActivity.getMainActivityId();

            object = getSubActivitiesBySECUnitCode("",institutionalPlan,id,startDateStr,endDateStr);
            if(object.length()>0)
                result.put(object);

        }
        return result;
    }

    private JSONArray getSubActivitiesByDIRUnitCode(String unitCode,String institutionalPlan, String startDateStr,String endDateStr) throws ParseException {
        JSONArray result = new JSONArray();

        List<Unit> DIVUnits = unitRepository.getUnitsUnderUnitCodeByUnitTypeCodes(unitCode,"DIV");

        for (Unit unit : DIVUnits) {

            JSONArray jsonArray = new JSONArray();
            jsonArray = getSubActivitiesByDIVUnitCode(unit.getUnitCode(),institutionalPlan,startDateStr,endDateStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                result.put(jsonObject);
            }

        }
        return result;
    }

    private String detectTaskColor(Integer progress,Date startDate,Date EndDate){
            String redColor = "#dc3545";
            String greenColor = "#28a745";
            String goldColor = "#b68a35";
            String notBeginColor = "#6c757d";
            String color  = "#165080";

            long diffFromEndDateAndStartDate = Utils.differenceBetweenTwoDatesWithoutABS( new Date(),EndDate);

            if(diffFromEndDateAndStartDate <= 0){
                if(progress < 100){
                    color = redColor;
                }else {
                    color = greenColor;
                }
                return color;
            }
             diffFromEndDateAndStartDate = Utils.differenceBetweenTwoDatesWithoutABS( startDate,new Date());

        if(diffFromEndDateAndStartDate <= 0){
            if(progress > 0){
                color = goldColor;
            }else {
                color = notBeginColor;
            }
            return color;
        }
        long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(startDate, EndDate);
            long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(startDate, new Date());

            long expectedProgress = 90;

            try
            {
                if(diffnow < 0){
                    color = notBeginColor;
                    return color;
                }
                expectedProgress = (diffnow/ diffTotal)*100;
            } catch (Exception e){
                e.printStackTrace();
            }
            if(progress > expectedProgress +10 ){
                    color = goldColor;
            } else if(progress< expectedProgress - 10){
               color = redColor;
            }else if (progress < expectedProgress + 10  && progress > expectedProgress - 10 ){
                color = greenColor;
            }
        return color;
    }

}

