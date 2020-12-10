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
import java.util.*;

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

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(endDate);
            cal.add(Calendar.DATE, -1);
            endDate = cal.getTime();

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
                int yearInt = Integer.parseInt(year);

                String fromDateString = general.getFromDate();
                String toDateString = general.getToDate();

                String fromDateYear = fromDateString.substring(0,fromDateString.indexOf("-"));
                String toDateYear = toDateString.substring(0,toDateString.indexOf("-"));

                int fromDateYearInt = Integer.parseInt(fromDateYear);
                int toDateYearInt = Integer.parseInt(toDateYear);

                if(fromDateYearInt == toDateYearInt){
                    //add year to fromDate
                    fromDateString = year + fromDateString.substring(fromDateString.indexOf("-"), fromDateString.length());
                    //add year to toDate
                    toDateString = year + toDateString.substring(toDateString.indexOf("-"), toDateString.length());

                    Date fromDate = simpleDateFormat.parse(fromDateString);
                    Date toDate = simpleDateFormat.parse(toDateString);

                    if (Utils.isDateBetweenDates(fromDate, startDate, endDate)
                            || Utils.isDateBetweenDates(toDate, startDate, endDate)) {
                        if (!hasExceptionInList(general, resultVacations)) {
                            general.setFromDate(fromDate);
                            general.setToDate(toDate);
                            resultVacations.add(general);
                        }
                    }
                }else{
                    //For Early Year
                    Vacation generalEarly = new Vacation(general);

                    //calculate the year of fromDate
                    int fromDateYearIntEarly = yearInt - (toDateYearInt - fromDateYearInt);
                    //add year to fromDate
                    String fromDateStringEarly = fromDateYearIntEarly + fromDateString.substring(fromDateString.indexOf("-"), fromDateString.length());
                    //add year to toDate
                    String toDateStringEarly = year + toDateString.substring(toDateString.indexOf("-"), toDateString.length());

                    Date fromDateEarly = simpleDateFormat.parse(fromDateStringEarly);
                    Date toDateEarly = simpleDateFormat.parse(toDateStringEarly);

                    if (Utils.isDateBetweenDates(fromDateEarly, startDate, endDate)
                            || Utils.isDateBetweenDates(toDateEarly, startDate, endDate)) {
                        if (!hasExceptionInList(generalEarly, resultVacations)) {
                            generalEarly.setFromDate(fromDateEarly);
                            generalEarly.setToDate(toDateEarly);
                            resultVacations.add(generalEarly);
                        }
                    }

                    //For Late Year
                    Vacation generalLate = new Vacation(general);
                    //add year to fromDate
                    String fromDateStringLate = year + fromDateString.substring(fromDateString.indexOf("-"), fromDateString.length());
                    //calculate the year of toDate
                    int toDateYearIntLate = yearInt + (toDateYearInt - fromDateYearInt);
                    //add year to toDate
                    String toDateStringLate = toDateYearIntLate + toDateString.substring(toDateString.indexOf("-"), toDateString.length());

                    Date fromDateLate = simpleDateFormat.parse(fromDateStringLate);
                    Date toDateLate = simpleDateFormat.parse(toDateStringLate);

                    if (Utils.isDateBetweenDates(fromDateLate, startDate, endDate)
                            || Utils.isDateBetweenDates(toDateLate, startDate, endDate)) {
                        if (!hasExceptionInList(generalLate, resultVacations)) {
                            generalLate.setFromDate(fromDateLate);
                            generalLate.setToDate(toDateLate);
                            resultVacations.add(generalLate);
                        }
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
