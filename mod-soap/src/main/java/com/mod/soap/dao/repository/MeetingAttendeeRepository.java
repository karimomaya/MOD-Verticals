package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.MeetingAttendee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by karim on 3/9/20.
 */
public interface MeetingAttendeeRepository extends GenericRepository<MeetingAttendee, Long>{
    @Query(value = "{call MOD_MM_SP_GetMeetingAttendees(:meetingId)}", nativeQuery = true)
    ArrayList<MeetingAttendee> getMeetingAttendeeData(@Param("meetingId") long meetingId);
}

