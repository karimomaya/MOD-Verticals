package com.mod.rest.repository;

import com.mod.rest.model.MinuteOfMeeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by omar.sabry on 1/9/2020.
 */
public interface MinuteOfMeetingRepository extends GenericRepository<MinuteOfMeeting, Long> {
    @Query(value = "{call MOD_MM_SP_GetMinutesOfMeetingByMeetingId(:meetingId)}", nativeQuery = true)
    ArrayList<MinuteOfMeeting> getMinutesOfMeeting(@Param("meetingId") long meetingId);
}
