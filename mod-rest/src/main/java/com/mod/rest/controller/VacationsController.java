package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.Vacation;
import com.mod.rest.repository.VacationRepository;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by omar.sabry on 12/9/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/vacations")
public class VacationsController {

    @Autowired
    VacationRepository vacationRepository;

    @GetMapping("get/{startDate}/{endDate}")
    @ResponseBody
    public ResponseBuilder<ObjectNode> getAll(
            @PathVariable("startDate") String startDateString,
            @PathVariable("endDate") String endDateString) {

        ResponseBuilder<ObjectNode> responseBuilder = new ResponseBuilder<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        try {
            List<Vacation> generalList = vacationRepository.findByRule("general");
            List<Vacation> exceptionList = vacationRepository.findByRule("exception");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = simpleDateFormat.parse(startDateString);
            Date endDate = simpleDateFormat.parse(endDateString);

            List<Vacation> resultVacations = new ArrayList<>();
            for (Vacation exception : exceptionList) {
                Date fromDate = simpleDateFormat.parse(exception.getFromDate());
                Date toDate = simpleDateFormat.parse(exception.getToDate());
                if (Utils.isDateBetweenDates(fromDate,startDate,endDate)
                        || Utils.isDateBetweenDates(toDate,startDate,endDate)) {
                    resultVacations.add(exception);
                }
            }
            for (Vacation general : generalList) {
                String year = startDateString.split("-")[0];

                String fromDateString = general.getFromDate();
                String toDateString = general.getToDate();

                String fromDateYear = fromDateString.substring(0,fromDateString.indexOf("-"));
                String toDateYear = toDateString.substring(0,toDateString.indexOf("-"));

                fromDateString = year + fromDateString.substring(fromDateString.indexOf("-"),fromDateString.length());

                if(fromDateYear.equals(toDateYear)){
                    toDateString = year + toDateString.substring(toDateString.indexOf("-"),toDateString.length());
                }else{
                    int yearInt = Integer.parseInt(year);
                    yearInt += 1;
                    toDateString = yearInt + toDateString.substring(toDateString.indexOf("-"),toDateString.length());
                }

                Date fromDate = simpleDateFormat.parse(fromDateString);
                Date toDate = simpleDateFormat.parse(toDateString);

                if (Utils.isDateBetweenDates(fromDate,startDate,endDate)
                        || Utils.isDateBetweenDates(toDate,startDate,endDate)) {
                    if(!hasExceptionInList(general, resultVacations)){
                        general.setFromDate(fromDate);
                        general.setToDate(toDate);
                        resultVacations.add(general);
                    }
                }
            }

            if (resultVacations.size() > 0) {
                result.put("vacations", Utils.writeObjectIntoString(resultVacations));
                responseBuilder.status(ResponseCode.SUCCESS);
            } else {
                responseBuilder.status(ResponseCode.NO_CONTENT);
            }
            return responseBuilder.data(result).build();
        }catch (ParseException e){
            e.printStackTrace();
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.data(result).build();
    }

    boolean hasExceptionInList(Vacation general, List<Vacation> exceptionList){
        for(Vacation exception: exceptionList){
            if(general.getId() == exception.getParent_Id()){
                return true;
            }
        }
        return false;
    }
}
