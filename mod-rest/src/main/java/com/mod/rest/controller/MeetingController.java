package com.mod.rest.controller;

import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    PDFService pdfService;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingAttendeeRepository meetingAttendeeRepository;
    @Autowired
    DiscussionPointRepository discussionPointRepository;
    @Autowired
    TaskReportHelperRepository taskReportHelperRepository;
    @Autowired
    TaskPerformerRepository taskPerformerRepository;

    @GetMapping("generate-minutes-of-meeting/{meetingId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateMinutesOfMeetings(@PathVariable("meetingId") long meetingId){
        System.out.println("generate meeting pdf: " + meetingId);
        HttpHeaders respHeaders = new HttpHeaders();
        Optional<Meeting> meetingOptional = meetingRepository.getMeetingData(meetingId);
        ArrayList<DiscussionPoint> discussionPoints = discussionPointRepository.getDiscussionPoints(meetingId);
//        ArrayList<Task> tasks = taskRepository.getTasksRelatedToDiscussionPointAndMeeting(meetingId);
        ArrayList<MeetingAttendee> tempMeetingAttendee = meetingAttendeeRepository.getMeetingAttendeeData(meetingId);
        ArrayList<MeetingAttendee> meetingAttendee = new ArrayList<>();
        for(MeetingAttendee attendee : tempMeetingAttendee){
            if(attendee.getAttendedMeeting()){
                meetingAttendee.add(attendee);
            }
        }
        ArrayList<TaskReportHelper> meetingTasks = new ArrayList<>();
        int count = 0;
        for (DiscussionPoint discussionPoint : discussionPoints){
            discussionPoint.setNumber(++count);
            ArrayList<TaskReportHelper> tasks = taskReportHelperRepository.getTasksRelatedToDiscussionPointAndMeeting(meetingId, discussionPoint.getId());
            for (TaskReportHelper task : tasks){
                ArrayList<TaskPerformer> performers = taskPerformerRepository.getPerformersByTaskId(1,1000000,task.getId());
                task.setPerformers(performers);
                task.setNumber(count);
                meetingTasks.add(task);
            }

        }

        if (meetingOptional.isPresent()) {

            Meeting meeting = meetingOptional.get();
            ArrayList<Meeting> arrayList = new ArrayList();
            arrayList.add(meeting);

            String templateName = pdfService.getTemplateName(meeting);
            System.out.println("get template name: " + templateName);

            try {
                File file = pdfService.generate(arrayList,templateName+".html", "meeting-data");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "meeting-subject");
                file = pdfService.generate(meetingAttendee, file.toURI().getPath(), "attendee-data");
                file = pdfService.generate(discussionPoints, file.toURI().getPath(), "discussion-point");
                file = pdfService.generate(meetingTasks, file.toURI().getPath(), "meeting-task");
                byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
                respHeaders.setContentLength(bytes.length);
                respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf" );
                return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }

    @GetMapping("room-timeline/{roomId}/{startDate}/{endDate}")
    public ResponseBuilder<String> roomTimeline(@RequestHeader("samlart") String SAMLart,
                                          @PathVariable("roomId") long roomId,
                                          @PathVariable("startDate") String startDateStr,
                                          @PathVariable("endDate") String endDateStr){

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<>();
        Date startDate = null;
        Date endDate = null;
        JSONArray result = null;

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);

            result = formulateRoomTimelineJSON(roomId, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseBuilder.data(result.toString()).build();
    }

    public JSONArray formulateRoomTimelineJSON(long roomId, Date startDate, Date endDate){
//        JSONArray result = new JSONArray();

//        JSONObject object = new JSONObject();
        List<Meeting> meetingList = meetingRepository.getRoomMeetings(roomId, startDate, endDate);
        JSONArray data = new JSONArray();

        if (meetingList.size() == 0) return data;

        for (Meeting meeting : meetingList){
//            object.put("subject", meeting.getSubject());
            JSONObject json = new JSONObject();
            json.put("x", meeting.getSubject());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(meeting.getEndDate().getTime());
            jsonArray.put(meeting.getStartDate().getTime());
            json.put("y", jsonArray);
            String color = "#acdce7";
            switch (meeting.getStatus()){
                case 2:
                    color = "#ff0000";
                    break;
                case 3:
                    color = "#ff0000";
                    break;
                case 10:
                    color = "#0DA616";
                    break;
                case 20:
                    color = "#d2d3e1";
                    break;
            }
            json.put("color", color);
            json.put("tooltip", meeting.getSubject());
            data.put(json);
        }

//        object.put("data", data);
//        result.put(object);

        return data;
    }
}
