package com.mod.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.Task;
import com.mod.rest.model.UserProductivitySP;
import com.mod.rest.repository.TaskRepository;
import com.mod.rest.system.Pagination;
import com.mod.rest.service.TargetTaskPerformerService;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karim.omaya on 11/1/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/target-task")
public class TargetTaskPerformerController  {


/*
    @GET
    @Path("get/delayed-table/{userId}/{start}/{end}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDelayedTasksTable(@PathParam("userId") long userId,@PathParam("start") int start,@PathParam("end") int end) {

        Response.ResponseBuilder response = Response
                .status(Response.Status.OK);
        Pagination<List<Task>> tasks = new TargetTaskPerformerService().executeDelayedTaskTable(userId, start, end);
        if(tasks != null && tasks.getData().size()>0){

            response.status(Response.Status.OK);
        }
        else {
            response.status(Response.Status.NO_CONTENT);
        }

        return Utils.createResponse(Utils.writeObjectIntoString(tasks),response);

    }


    @GET
    @Path("get/delayed/{userId}/{start}/{end}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDelayedTasksGraph(@PathParam("userId") long userId,@PathParam("start") int start,@PathParam("end") int end) {

        Response.ResponseBuilder response = Response
                .status(Response.Status.OK);

        TargetTaskPerformerService targetTaskPerformerService =  new TargetTaskPerformerService();

        int[] graph= targetTaskPerformerService.getDelayedTasks(userId);


        Pagination<List<Task>> tasks = targetTaskPerformerService.executeDelayedTaskTable(userId, start, end);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data= mapper.createObjectNode();


        if(tasks != null && tasks.getData().size()>0){
            response.status(Response.Status.OK);
            data.put("data", Arrays.toString(graph));
            data.put("tasks", Utils.writeObjectIntoString(tasks));
        }
        else {
            response.status(Response.Status.NO_CONTENT);
        }

        return Utils.createResponse(Utils.writeObjectIntoString(data),response);
    }


    @GET
    @Path("get/user-productivity-table/{userId}/{startDate}/{endDate}/{userIds}/{start}/{end}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProductivityReportTable(@PathParam("userId") long userId,@PathParam("startDate")  String startDateStr,
                                              @PathParam("endDate") String endDateStr, @PathParam("userIds") String userIds,
                                              @PathParam("start") int start, @PathParam("end") int end) {

        Response.ResponseBuilder response = Response
                .status(Response.Status.OK);


        Date startDate = Utils.convertStringToDate(startDateStr);
        Date endDate = Utils.convertStringToDate(endDateStr);

        Pagination<List<UserProductivitySP>> result = new TargetTaskPerformerService().executeUserProductivityReport(userId,
                startDate, endDate,userIds, start, end);

        if (result == null){
            response.status(Response.Status.BAD_REQUEST);
        }
        else if (result.getData().size() == 0) {
            response.status(Response.Status.NO_CONTENT);
        }

        return Utils.createResponse(Utils.writeObjectIntoString(Utils.writeObjectIntoString(result)), response);
    }


//    user-productivity/164020230/2019-10-01/2019-10-31/%3B655622146%3B655687685%3B/0/10

    @GET
    @Path("get/user-productivity/{userId}/{startDate}/{endDate}/{userIds}/{start}/{end}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProductivityReport(@PathParam("userId") long userId,@PathParam("startDate")  String startDateStr,
                                              @PathParam("endDate") String endDateStr, @PathParam("userIds") String userIds,
                                              @PathParam("start") int start, @PathParam("end") int end) {
        Response.ResponseBuilder response = Response
                .status(Response.Status.OK);

        TargetTaskPerformerService targetTaskPerformerService = new TargetTaskPerformerService();

        Date startDate = Utils.convertStringToDate(startDateStr);
        Date endDate = Utils.convertStringToDate(endDateStr);

        Pagination<List<UserProductivitySP>> result = targetTaskPerformerService.executeUserProductivityReport(userId,
                startDate, endDate,userIds, start, end);

        HashMap<Long, TargetTaskPerformerService.ReportResult> graphData = targetTaskPerformerService.executeUserProductivityReport(userId,
                startDate, endDate,userIds);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data= mapper.createObjectNode();

        if (result == null){
            response.status(Response.Status.BAD_REQUEST);
        }
        else if (result.getData().size() == 0) {
            response.status(Response.Status.NO_CONTENT);
        }
        else {
            response.status(Response.Status.OK);
            data.put("data", Utils.writeObjectIntoString(graphData));
            data.put("tasks", Utils.writeObjectIntoString(result));
        }
        return Utils.createResponse(Utils.writeObjectIntoString(data), response);
    }

*/
}
