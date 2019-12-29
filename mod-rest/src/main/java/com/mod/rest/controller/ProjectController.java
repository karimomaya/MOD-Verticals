package com.mod.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.Project;
import com.mod.rest.system.Pagination;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.service.ProjectService;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karim.omaya on 11/4/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/project")
public class ProjectController  {

    @Autowired
    ProjectService projectService;


    @GetMapping("get/projects-report-table/{userId}/{projectId}/{startDate}/{endDate}/{pageNumber}/{pageSize}")
    public ResponseBuilder<List<Project>> getProjectsReportTable(@PathVariable("userId") long userId, @PathVariable("projectId") long projectId,
                                                  @PathVariable("startDate") String startDateStr, @PathVariable("endDate") String endDateStr,
                                                  @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        ResponseBuilder<List<Project>> responseBuilder = new ResponseBuilder<List<Project>>();


        Date startDate = Utils.convertStringToDate(startDateStr);
        Date endDate = Utils.convertStringToDate(endDateStr);


        long count = projectService.countProjectByDate(startDate, endDate, userId);

        List<Project> projects = projectService.getProjectsReportTable(userId, startDate, endDate, pageNumber, pageSize);

        responseBuilder.data(projects);


        if(projects == null || projects.size() == 0){
            responseBuilder.status(ResponseCode.NO_CONTENT);
        }
        else {
            Pagination pagination = new Pagination();
            pagination.setPageNumber(pageNumber).setNumberOfResults(count).setPageSize(pageSize).build();
            responseBuilder.setPagination(pagination);
            responseBuilder.status(ResponseCode.SUCCESS);
        }

        return responseBuilder.build();
    }

    @GetMapping("get/projects-report-graph/{userId}/{projectId}/{startDate}/{endDate}/{pageNumber}/{pageSize}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    public ResponseBuilder<String> getProjectsReportGraph(@PathVariable("userId") long userId, @PathVariable("projectId") long projectId,
                                           @PathVariable("startDate") String startDateStr, @PathVariable("endDate") String endDateStr,
                                           @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        ResponseBuilder<String> responseBuilder = new ResponseBuilder<String>();

        ProjectService projectService = new ProjectService();
        Date startDate = Utils.convertStringToDate(startDateStr);
        Date endDate = Utils.convertStringToDate(endDateStr);
        HashMap<Integer, ProjectService.ProjectResult> graphData = projectService.getProjectsReportGraph(userId, startDate, endDate);

        List<Project> projects = projectService.getProjectsReportTable(userId, startDate, endDate, pageNumber, pageSize);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data= mapper.createObjectNode();

        if(projects != null && projects.size()>0){
            data.put("data", Utils.writeObjectIntoString(graphData));
            data.put("projects", Utils.writeObjectIntoString(projects));
            responseBuilder.data(Utils.writeObjectIntoString(data));
            responseBuilder.status(ResponseCode.SUCCESS);
        }
        else {
            responseBuilder.status(ResponseCode.NO_CONTENT);
        }

        return responseBuilder.build();
    }


}
