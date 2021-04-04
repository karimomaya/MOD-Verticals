package com.mod.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.rest.model.IndividualReport;
import com.mod.rest.model.MeetingAttendee;
import com.mod.rest.model.User;
import com.mod.rest.model.Vacation;
import com.mod.rest.repository.IndividualRepository;
import com.mod.rest.repository.MeetingAttendeeRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.repository.VacationRepository;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by omar.sabry on 12/13/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    VacationRepository vacationRepository;
    @Autowired
    MeetingAttendeeRepository meetingAttendeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IndividualRepository individualRepository;

    @GetMapping("available/")
    @ResponseBody
    public ResponseBuilder<ObjectNode> isUserAvailable(
            @RequestParam("userEntityId") String userEntityId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("isExternal") Boolean isExternal,
            @RequestParam(name = "exceptionMeetings", required = false) String exceptionMeetings) {

        ResponseBuilder<ObjectNode> responseBuilder = new ResponseBuilder<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();


        List<String> users = new ArrayList<>();

        if (!userEntityId.isEmpty()) {
            String[] usersIds = userEntityId.split(",");
            if (isExternal) {
                for (String userId : usersIds) {
                    Optional<IndividualReport> individual = individualRepository.findById(Long.parseLong(userId));
                    if (individual.isPresent()) {
                        if (!users.contains(individual.get().getNameArabic())) {
                            users.add(individual.get().getNameArabic());
                        }
                    }
                }
            } else {
                for (String userId : usersIds) {
                    Optional<User> user = userRepository.findById(Long.parseLong(userId));
                    if (user.isPresent()) {
                        if (!users.contains(user.get().getDisplayName())) {
                            users.add(user.get().getDisplayName());
                        }
                    }
                }
            }
        }

        if (startDate.split("T")[0].equals(endDate.split("T")[0])) {
            try {
                int startDateDay = Utils.getDayNameFromDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate.split("T")[0]));
                int endDateDay = Utils.getDayNameFromDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate.split("T")[0]));
                if (startDateDay == 5 || startDateDay == 6 || endDateDay == 5 || endDateDay == 6) {
                    result.put("available", false);
                    result.put("usersDisplayNames", Utils.writeObjectIntoString(users));
                    responseBuilder.status(ResponseCode.SUCCESS);
                    return responseBuilder.data(result).build();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }


            List<Vacation> vacationList = vacationRepository.getByDate(startDate.split("T")[0], endDate.split("T")[0]);

            if (vacationList.size() > 0) {
                result.put("available", false);
                result.put("usersDisplayNames", Utils.writeObjectIntoString(users));
                responseBuilder.status(ResponseCode.SUCCESS);
                return responseBuilder.data(result).build();
            }
        }

        List<MeetingAttendee> attendeeList = meetingAttendeeRepository.getConflictAttendee(startDate, endDate, userEntityId, isExternal);

        ArrayList<String> attendeesDisplayName = new ArrayList<>();

        for (MeetingAttendee attendee : attendeeList) {
            if (isMeetingException(attendee.getMeeting_to_attendees_Id(), exceptionMeetings)) {
                continue;
            } else {
                if (!attendeesDisplayName.contains(attendee.getDisplayName())) {
                    attendeesDisplayName.add(attendee.getDisplayName());
                }
            }
        }

        if (attendeesDisplayName.size() > 0) {
            result.put("available", false);
            result.put("usersDisplayNames", Utils.writeObjectIntoString(attendeesDisplayName));
        } else {
            result.put("available", true);
        }

        responseBuilder.status(ResponseCode.SUCCESS);
        return responseBuilder.data(result).build();
    }

    private boolean isMeetingException(String meetingId, String exceptionMeetings) {
        if (exceptionMeetings == null) {
            return false;
        } else if (exceptionMeetings.isEmpty()) {
            return false;
        } else {
            String[] exceptionMeetingsList = exceptionMeetings.split(",");
            for (int i = 0; i < exceptionMeetingsList.length; i++) {
                if (exceptionMeetingsList[i].equals(meetingId)) {
                    return true;
                }
            }
            return false;
        }
    }
}
