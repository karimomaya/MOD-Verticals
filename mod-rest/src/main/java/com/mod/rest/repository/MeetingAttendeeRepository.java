package com.mod.rest.repository;


import com.mod.rest.model.MeetingAttendee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar.sabry on 1/8/2020.
 */
public interface MeetingAttendeeRepository extends GenericRepository<MeetingAttendee, Long>{
    @Query(value = "{call MOD_MM_SP_GetMeetingAttendees(:meetingId)}", nativeQuery = true)
    ArrayList<MeetingAttendee> getMeetingAttendeeData(@Param("meetingId") long meetingId);

    @Query(value = "{call MOD_MM_SP_GetConflictAttendee(:startTime,:endTime,:attendeeId, :isExternal)}", nativeQuery = true)
    List<MeetingAttendee> getConflictAttendee(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("attendeeId") String attendeeId, @Param("isExternal") boolean isExternal);
}
