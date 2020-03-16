package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by karim on 3/9/20.
 */
public interface MeetingRepository extends GenericRepository<Meeting, Long> {
    @Query(value = "{call MOD_MM_SP_ReadMeetingData(:entityId)}", nativeQuery = true)
    Optional<Meeting> getMeetingData(@Param("entityId") long entityId);

    @Query(value = "{call MOD_MM_SP_PeriodicMeeting(:entityId, :meetingSubject)}", nativeQuery = true)
    ArrayList<Meeting> getPeriodicMeeting(@Param("entityId") long entityId, @Param("meetingSubject") String meetingSubject);
}

