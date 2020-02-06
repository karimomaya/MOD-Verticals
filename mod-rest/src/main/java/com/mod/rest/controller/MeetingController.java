package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.*;
import com.mod.rest.repository.MeetingAttendeeRepository;
import com.mod.rest.repository.MeetingRepository;
import com.mod.rest.repository.MinuteOfMeetingRepository;
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
import java.nio.file.Files;
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
    MinuteOfMeetingRepository minuteOfMeetingRepository;

    @GetMapping("generate-minutes-of-meeting/{meetingId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateMinutesOfMeetings(@PathVariable("meetingId") long meetingId){
        HttpHeaders respHeaders = new HttpHeaders();
        Optional<Meeting> meetingOptional = meetingRepository.getMeetingData(meetingId);
        ArrayList<MeetingAttendee> meetingAttendee = meetingAttendeeRepository.getMeetingAttendeeData(meetingId);
        ArrayList<MinuteOfMeeting> minutesOfMeeting = minuteOfMeetingRepository.getMinutesOfMeeting(meetingId);
        if (meetingOptional.isPresent()) {
            Meeting meeting = meetingOptional.get();
            ArrayList<Meeting> arrayList = new ArrayList();
            arrayList.add(meeting);
            String templateName = pdfService.getTemplateName(meeting);

            try {
                File file = pdfService.generate(arrayList,"pdf-template/"+templateName+".html", "meeting-data");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "meeting-description");
                file = pdfService.generate(meetingAttendee, file.toURI().getPath(), "attendee-data");
                file = pdfService.generate(minutesOfMeeting, file.toURI().getPath(), "minute-of-meeting");
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
                case 10:
                    color = "#0DA616";
                    break;
                case 20:
                    color = "#ff0000";
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
