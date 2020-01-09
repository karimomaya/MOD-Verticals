package com.mod.rest.repository;

import com.mod.rest.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by omar.sabry on 1/8/2020.
 */
public interface MeetingRepository extends GenericRepository<Meeting, Long> {
    @Query(value = "{call MOD_MM_SP_ReadMeetingData(:entityId)}", nativeQuery = true)
    Optional<Meeting> getMeetingData(@Param("entityId") long entityId);
}
