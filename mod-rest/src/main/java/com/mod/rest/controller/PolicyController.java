package com.mod.rest.controller;

import com.mod.rest.model.PolicyActivity;
import com.mod.rest.model.PolicyMilestone;
import com.mod.rest.repository.PolicyActivityRepository;
import com.mod.rest.repository.PolicyMilestoneRepository;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Created by omaradl on 5/18/2020.
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/policy")
public class PolicyController {
    @Autowired
    PolicyMilestoneRepository policyMilestoneRepository;
    @Autowired
    PolicyActivityRepository policyActivityRepository;

    @Autowired
    private Environment env;

    @GetMapping("workplan-timeline/{workplanId}")
    public ResponseBuilder<String> workplanTimeline(@RequestHeader("samlart") String SAMLart,
                                                @PathVariable("workplanId") long workplanId){

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();
        JSONArray result = null;

        result = formulateWorkplanTimelineJSON(workplanId);

        return responseBuilder.data(result.toString()).build();
    }

    public JSONArray formulateWorkplanTimelineJSON(long workplanId){
        List<PolicyMilestone> milestones = policyMilestoneRepository.getWorkplanMilestones(workplanId);
        JSONArray result = new JSONArray();

        if (milestones.size() == 0) return result;

        for (PolicyMilestone milestone : milestones) {
            List<PolicyActivity> activities = policyActivityRepository.getMileStoneActivites(milestone.getId());

            JSONObject json = new JSONObject();
            json.put("name", "");
            json.put("x", milestone.getMileStoneName());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(milestone.getEndDate().getTime());
            jsonArray.put(milestone.getStartDate().getTime());
            json.put("y", jsonArray);
            String color = detectProgressColor(milestone.getStartDate(), milestone.getEndDate(), milestone.getProgressBar());
            json.put("color", color);
            json.put("tooltip", milestone.getProgressBar());
            result.put(json);

            int sequence = 1;
            for (PolicyActivity activity : activities) {

                json = new JSONObject();
                json.put("name", activity.getActivity_name());
                json.put("x", ""+ sequence++ +"");
                jsonArray = new JSONArray();
                jsonArray.put(activity.getActivity_end_date().getTime());
                jsonArray.put(activity.getActivity_start_date().getTime());
                json.put("y", jsonArray);
                color = detectProgressColor(activity.getActivity_start_date(), activity.getActivity_end_date(), activity.getActivity_progress_bar());
                json.put("color", color);
                json.put("tooltip", activity.getActivity_progress_bar());
                result.put(json);
            }
        }

        return result;
    }

    private String detectProgressColor(Date startDate, Date endDate, Integer progress){

        String result = Utils.getProgressColor(progress, startDate, endDate);

        String color = "#"+env.getProperty(result);

        /*
        String color  = "#165080";

        long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(startDate, endDate);
        long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(startDate, new Date());

        long expectedProgress = 90;

        try
        {
            expectedProgress = (diffnow/ diffTotal)*100;
        } catch (Exception e){

        }



        if (progress == 100) {
            color  = "#38A32B";
        }else if(progress > expectedProgress ){
            color = "#4aa472";
        }else if (progress < expectedProgress ) {
            color = "#d44e5a";
        }else if (progress < expectedProgress + 10  && progress > expectedProgress - 10 ){
            color = "#c9a869";
        }

         */

        return color;
    }
}
