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
    TaskPerformerHelperRepository taskPerformerHelperRepository ;
    @Autowired
    UserHelperService userHelperService;

    @GetMapping("task-timeline/{startDate}/{endDate}/{users}")
    public ResponseBuilder<String> report(@RequestHeader("samlart") String SAMLart,
                                          @PathVariable("startDate") String startDateStr,
                                          @PathVariable("endDate") String endDateStr,
                                          @PathVariable("users") String usersStr){

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();
//YYYY-MM-DD
        Date startDate = null;
        Date endDate = null;
        JSONArray result = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
            if (usersStr.equals(";")){
                result = formulateJSONWithAllUsers(SAMLart, startDate, endDate);
            }
            else {
                result = formulateJSONWithSpecificUsers(SAMLart, startDate, endDate, usersStr.split(","));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return responseBuilder.data(result.toString()).build();

    }


    public JSONArray formulateJSONWithSpecificUsers(String SAMLart, Date startDate, Date endDate, String[] users){
        JSONArray result = new JSONArray();

        for (int i=0; i< users.length; i++){

            JSONObject object = new JSONObject();

            List<TaskPerformerHelper> taskList = taskPerformerHelperRepository.getTasksByPerformerId(Long.parseLong(users[i]), startDate, endDate);
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

                data.put(json);
            }
            setUsername = false;
            object.put("data", data);
            result.put(object);

        }
        return result;
    }


    private String detectTaskColor(TaskPerformerHelper task){
        String color  = "#0F8BD0";
        int taskStatus = task.getTaskStatus();
        int performerStatus = task.getTargetPerformerStatus();
        Date today = new Date();
        Date dueDate = task.getDueDate();
        int dateState = Utils.compareDates(today, dueDate); // 1 delayed

        if (taskStatus ==3){
            color  = "#38A32B";
        } else if (taskStatus !=3 && dateState == 1 && performerStatus != 2){
            color = "#D44E5A";
        } else if (performerStatus == 2){
            color = "#C9A869";
        }else {
            color = "#F5E630";
        }
        return color;
    }

    public JSONArray formulateJSONWithAllUsers(String SAMLart, Date startDate, Date endDate){
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

            List<TaskPerformerHelper> taskList = taskPerformerHelperRepository.getTasksByPerformerId(userIdLong, startDate, endDate);
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

                data.put(json);
            }

            object.put("data", data);
            result.put(object);

        }
        return result;
    }

}
