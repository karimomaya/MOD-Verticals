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

            for (PolicyActivity activity : activities) {
                JSONObject object = new JSONObject();
                object.put("name", activity.getActivity_name());
                JSONArray data = new JSONArray();

                JSONObject json = new JSONObject();
                json.put("x", milestone.getMileStoneName());
                JSONArray jsonArray = new JSONArray();

                jsonArray.put(activity.getActivity_end_date().getTime());
                jsonArray.put(activity.getActivity_start_date().getTime());
                json.put("y", jsonArray);
                String color = detectActivityColor(activity);

                json.put("color", color);
                json.put("tooltip",activity.getActivity_progress_bar());

                data.put(json);

                object.put("data", data);
                result.put(object);
            }
        }

        return result;
    }

    private String detectActivityColor(PolicyActivity activity){

        String color  = "#165080";

        long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(activity.getActivity_start_date(), activity.getActivity_end_date());
        long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(activity.getActivity_start_date(), new Date());

        long expectedProgress = 90;

        try
        {
            expectedProgress = (diffnow/ diffTotal)*100;
        } catch (Exception e){

        }

        if (activity.getActivity_progress_bar() == 100) {
            color  = "#38A32B";
        }else if(activity.getActivity_progress_bar() > expectedProgress ){
            color = "#4aa472";
        }else if (activity.getActivity_progress_bar() < expectedProgress ) {
            color = "#d44e5a";
        }else if (activity.getActivity_progress_bar() < expectedProgress + 10  && activity.getActivity_progress_bar() > expectedProgress - 10 ){
            color = "#c9a869";
        }

        return color;
    }
}
