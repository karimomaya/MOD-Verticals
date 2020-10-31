package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.LookupService;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by abdallah.shaaban on 7/16/2020.
 */

@Data
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/institutionalPlan")
public class InstitutionalPlanController {
    @Autowired
    LookupService lookupService;
    @Autowired
    IPOperationalIndicatorRepository ipOperationalIndicatorRepository;
    @Autowired
    LookupRepository lookupRepository;
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

        JSONArray result = new JSONArray();
        try {
            if(unitTypeCode.equals( "SEC")){
                JSONObject object = new JSONObject();
                object = getSubActivitiesBySECUnitCode(unitCode,institutionalPlan, null,startDateStr,endDateStr);

                result.put(object);
            }else if (unitTypeCode.equals("DIV")){
                result = getSubActivitiesByDIVUnitCode(unitCode,institutionalPlan,startDateStr,endDateStr);
            }else {
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
        List<IPSubActivity> subActivitiesList = new ArrayList<>();
        subActivitiesList  = ipSubActivityRepository.getIPSubActivity(unitCode,institutionalPlan,mainActivityId, startDate,endDate);
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
//            DecimalFormat df = new DecimalFormat("###.##");
//            subActivity.setProgress( Float.parseFloat((df.format(subActivity.getProgress()))));
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
//            DecimalFormat df = new DecimalFormat("###.##");
//            mainActivity.setProgress( df.format(mainActivity.getProgress()));
            Float progress= Float.parseFloat(mainActivity.getProgress());

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

    private String detectTaskColor(Float progress,Date startDate,Date EndDate){
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


        @GetMapping ("/SubActivity/ReportChart/{language}/{unitTypeCode}/{unitCode}/{institutionalPlan}/{startDate}/{endDate}/{typeOfAssignment}")
    public ResponseBuilder<String> subActivityReportChart(@PathVariable("language") String language,@PathVariable("unitCode") String unitCode,@PathVariable("unitTypeCode") String unitTypeCode
            ,@PathVariable("institutionalPlan") String institutionalPlan,@PathVariable("startDate") String startDateStr
            ,@PathVariable("endDate") String endDateStr,@PathVariable("typeOfAssignment") String typeOfAssignment) throws ParseException {

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result= mapper.createObjectNode();


        List<GraphDataHelper> graphDataHelpers = null;
        String[] xaxis = null;

        HashMap<List, List> hashMap = subActivitiesReportChartHelper(language,unitCode , unitTypeCode,institutionalPlan,startDateStr,endDateStr,typeOfAssignment);

        for (Map.Entry<List, List> entry : hashMap.entrySet()) {
            graphDataHelpers = entry.getKey();
            java.lang.Object[] objArr =  entry.getValue().toArray();
            xaxis = Arrays.copyOf(objArr, objArr.length,String[].class);
        }
        result.put("graph", Utils.writeObjectIntoString(graphDataHelpers));
        result.put("xaxis", Utils.writeObjectIntoString(xaxis));

        responseBuilder.status(ResponseCode.SUCCESS);
        return responseBuilder.data(result.toString()).build();
    }

    private HashMap<List, List> subActivitiesReportChartHelper(String language,String unitCode,String unitTypeCode,String institutionalPlan,String startDateStr,String endDateStr , String typeOfAssignment) throws ParseException {
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        List<IPSubActivity> subActivityList = new ArrayList<>();
        GraphDataHelper graphDataHelper = null;
        ArrayList<String> xaxis = new ArrayList<>();


        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);



        if(unitTypeCode.equals("SEC") || unitCode.equals("EGD")){
            subActivityList = ipSubActivityRepository.getSubActivitiesBySECUnitCodes(unitCode, institutionalPlan, startDate, endDate,typeOfAssignment );
        }else{
            List<Unit> SECUnits = unitRepository.getUnitsUnderUnitCodeByUnitTypeCodes(unitCode,"SEC");
            String units ="";
            for (Unit unit : SECUnits){
                units = units + unit.getUnitCode()+",";
            }
            subActivityList = ipSubActivityRepository.getSubActivitiesBySECUnitCodes(units, institutionalPlan, startDate, endDate,typeOfAssignment );

        }


        List<Lookup> lookUps = lookupRepository.getLookupByCategory("typeOfAssignment");

        int[] countArray = new int[5];
        for (IPSubActivity subActivity : subActivityList){
            int assignmentType = Integer.parseInt(subActivity.getClassification());
            if(assignmentType > 0 && assignmentType < 6){
                countArray[assignmentType-1] += 1;
            }
        }

        graphDataHelper = new GraphDataHelper();
        graphDataHelper.setName("نشاط فرعي");
//        graphDataHelper.setData(countArray);
        graphDataHelpers.add(graphDataHelper);

        List<Integer> countArrayList =new ArrayList<>();
        for (Lookup lookUp : lookUps){
            if(lookUp.getValueByLanguage(language) != null ){
            int key = Integer.parseInt(lookUp.getKey());
                if(countArray[key-1]>0) {
                    xaxis.add(lookUp.getValueByLanguage(language));
                    countArrayList.add(countArray[key - 1]);
                }
            }
        }
        int[] countArrayOutput = new int[countArrayList.size()];
        countArrayOutput = countArrayList.stream().mapToInt(Integer::intValue).toArray();
        graphDataHelper.setData(countArrayOutput);

        HashMap<List, List> result = new HashMap<>();
        result.put(graphDataHelpers, xaxis);


        return result;

    }



    @GetMapping("/subActivitiesOverClassification/export/Excel/{language}/{institutionalPlan}/{unitTypeCode}/{unitCode}/{startDate}/{endDate}/{typeOfAssignment}")
    public ResponseEntity<byte[]> exporSubActivitiesOverClassificationToExcel(@PathVariable("language") String language,@PathVariable("unitCode") String unitCode,@PathVariable("unitTypeCode") String unitTypeCode
            ,@PathVariable("institutionalPlan") String institutionalPlan,@PathVariable("startDate") String startDateStr,@PathVariable("endDate") String endDateStr,@PathVariable("typeOfAssignment")String typeOfAssignment) throws ParseException {

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);


        List<IPSubActivity> subActivityList = new ArrayList<>();
        if(unitTypeCode.equals("SEC") || unitCode.equals("EGD")){
            subActivityList = ipSubActivityRepository.getSubActivitiesBySECUnitCodes(unitCode, institutionalPlan, startDate, endDate,typeOfAssignment );
        }else{
            List<Unit> SECUnits = unitRepository.getUnitsUnderUnitCodeByUnitTypeCodes(unitCode,"SEC");
            String units ="";
            for (Unit unit : SECUnits){
                units = units + unit.getUnitCode()+",";
            }
            subActivityList = ipSubActivityRepository.getSubActivitiesBySECUnitCodes(units, institutionalPlan, startDate, endDate,typeOfAssignment );
        }
        lookupService.substituteLookupIds(subActivityList,"typeOfAssignment","classification",language);
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;

        try {
            file = excelWriterService.generate(subActivityList);
            if (file == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null);
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

    @GetMapping("/operationIndicator/export/Excel/{language}/{institutionalPlan}/{unitCode}/{quarter}")
    public ResponseEntity<byte[]> exportOperationalIndicatorToExcel(@PathVariable("language") String language,@PathVariable("unitCode") String unitCode
            ,@PathVariable("institutionalPlan") String institutionalPlan,@PathVariable("quarter")String quarter) throws ParseException {




        List<IPOperationalIndicatorReport> operationalIndicator = new ArrayList<>();
        operationalIndicator = ipOperationalIndicatorRepository.getOperationalIndicatorOfInstitutionalPlanByUnitCode( institutionalPlan,unitCode, quarter );

        lookupService.substituteLookupIds(operationalIndicator,"annualQuarters","quarter",language);
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;

        try {
            file = excelWriterService.generate(operationalIndicator);
            if (file == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"null\"").body(null);
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

