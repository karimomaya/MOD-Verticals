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
import java.util.ArrayList;
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
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {

        ResponseBuilder<ObjectNode> responseBuilder = new ResponseBuilder<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        Iterable<Vacation> vacationList = vacationRepository.findAll();
        List<Vacation> resultVacations = new ArrayList<>();
        for (Vacation vacation: vacationList) {
//            if(vacation.getRule().equals("general")){
//
//            }else{
//
//            }
            resultVacations.add(vacation);
        }
        if(resultVacations.size() > 0){
            result.put("vacations", Utils.writeObjectIntoString(resultVacations));
            responseBuilder.status(ResponseCode.SUCCESS);
        }
        else {
            responseBuilder.status(ResponseCode.NO_CONTENT);
        }

        return responseBuilder.data(result).build();
    }
}
