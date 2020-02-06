package com.mod.rest.controller;

import com.mod.rest.model.Task;
import com.mod.rest.model.TaskPerformerHelper;
import com.mod.rest.repository.TaskPerformerHelperRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.service.*;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by karim.omaya on 10/29/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/task")
public class TaskController  {

    @Autowired
    TaskPerformerHelperRepository taskPerformerHelperRepository;
    @Autowired
    UserHelperService userHelperService;

    @GetMapping("task-timeline/{startDate}/{endDate}/{users}/{project}/{status}")
    public ResponseBuilder<String> report(@RequestHeader("samlart") String SAMLart,
                                          @PathVariable("startDate") String startDateStr,
                                          @PathVariable("endDate") String endDateStr,
                                          @PathVariable("users") String usersStr,
                                          @PathVariable("project") int project,
                                          @PathVariable("status") int status){

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();
//YYYY-MM-DD
        Date startDate = null;
        Date endDate = null;
        JSONArray result = null;
        if (project == 0) {
            project = -1;
        }
        if (status == 0 || status == 4) {
            status = 0;
        }else if (status == 2) {
            status = 100;
        }else if (status == 1){
            status = 50;
        }

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
            if (usersStr.equals(";")){
                result = formulateJSONWithAllUsers(SAMLart, startDate, endDate, project, status);
            }
            else {
                result = formulateJSONWithSpecificUsers(startDate, endDate, usersStr.split(","), project, status);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return responseBuilder.data(result.toString()).build();

    }


    public JSONArray formulateJSONWithSpecificUsers( Date startDate, Date endDate, String[] users, int project, int status){
        JSONArray result = new JSONArray();

        for (int i=0; i< users.length; i++){

            JSONObject object = new JSONObject();

            List<TaskPerformerHelper> taskList = taskPerformerHelperRepository.getTasksByPerformerId(Long.parseLong(users[i]), startDate, endDate, project, status);
            if (taskList.size() == 0) continue;

            JSONArray data = new JSONArray();

            boolean setUsername = false;
            for (TaskPerformerHelper task : taskList){
                if (!setUsername){
                    object.put("name", task.getDisplayName());
                    setUsername = true;
                }
                JSONObject json = new JSONObject();
                json.put("x", task.getTaskName());
                JSONArray jsonArray = new JSONArray();

                jsonArray.put(task.getDueDate().getTime());
                jsonArray.put(task.getStartDate().getTime());
                json.put("y", jsonArray);
                String color = detectTaskColor(task);
                json.put("color", color);

                json.put("tooltip", "Percentage: <br> "+task.getProgress()+"");

                data.put(json);
            }
            setUsername = false;
            object.put("data", data);
            result.put(object);

        }
        return result;
    }


    private String detectTaskColor(TaskPerformerHelper task){





        String color  = "#165080";
        int taskStatus = task.getTaskStatus();

        if (taskStatus == 3){
            color = "#165080";
        }

        else {

            long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(task.getStartDate(), task.getDueDate());
            long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(task.getStartDate(), new Date());


            long expectedProgress = 90;

            try
            {
                expectedProgress = (diffnow/ diffTotal)*100;
            } catch (Exception e){

            }



            if (task.getProgress() == 100) {
                color  = "#38A32B";
            }else if (task.getProgress() < expectedProgress + 10  && task.getProgress() > expectedProgress - 10 ){
                color = "#c9a869";
            }else if(task.getProgress() > expectedProgress   ){
                color = "#4aa472";
            }else if (task.getProgress() < expectedProgress ) {
                color = "#d44e5a";
            }
        }


        return color;
    }

    public JSONArray formulateJSONWithAllUsers(String SAMLart, Date startDate, Date endDate, int project, int status){
        Document document =  userHelperService.getSubUsersDocument(SAMLart);
        NodeList userId = document.getElementsByTagName("UserEntityId");
        NodeList userName = document.getElementsByTagName("DisplayName");

        JSONArray result = new JSONArray();

        for (int i=0; i< userId.getLength(); i++){

            JSONObject object = new JSONObject();
            Node idNode = userId.item(i);
            Node nameNode = userName.item(i);
            Long userIdLong = Long.parseLong(idNode.getTextContent());
            String userNameStr = nameNode.getTextContent()+"";

            List<TaskPerformerHelper> taskList = taskPerformerHelperRepository.getTasksByPerformerId(userIdLong, startDate, endDate, project, status);
            if (taskList.size() == 0) continue;;

            object.put("name", userNameStr);
            JSONArray data = new JSONArray();

            for (TaskPerformerHelper task : taskList){
//                task.getDueDate()
                JSONObject json = new JSONObject();
                json.put("x", task.getTaskName());
                JSONArray jsonArray = new JSONArray();

                jsonArray.put(task.getDueDate().getTime());
                jsonArray.put(task.getStartDate().getTime());
                json.put("y", jsonArray);
                String color = detectTaskColor(task);

                json.put("color", color);
                json.put("tooltip",task.getProgress());

                data.put(json);
            }

            object.put("data", data);
            result.put(object);

        }
        return result;
    }

}
