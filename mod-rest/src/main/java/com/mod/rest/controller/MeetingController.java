package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.rest.model.Meeting;
import com.mod.rest.model.MeetingAttendee;
import com.mod.rest.model.MinuteOfMeeting;
import com.mod.rest.model.ReportObject;
import com.mod.rest.repository.MeetingAttendeeRepository;
import com.mod.rest.repository.MeetingRepository;
import com.mod.rest.repository.MinuteOfMeetingRepository;
import com.mod.rest.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
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
}
