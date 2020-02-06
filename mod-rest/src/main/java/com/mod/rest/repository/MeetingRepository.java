package com.mod.rest.repository;

import com.mod.rest.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by omar.sabry on 1/8/2020.
 */
public interface MeetingRepository extends GenericRepository<Meeting, Long> {
    @Query(value = "{call MOD_MM_SP_ReadMeetingData(:entityId)}", nativeQuery = true)
    Optional<Meeting> getMeetingData(@Param("entityId") long entityId);

    @Query(value = "{call MOD_MM_SP_GetRoomMeetings(:roomId, :startDate, :endDate)}", nativeQuery = true)
    List<Meeting> getRoomMeetings(@Param("roomId") long roomId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
