package com.mod.rest.repository;

import com.mod.rest.model.DiscussionPoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by karim on 2/16/20.
 */
public interface DiscussionPointRepository extends GenericRepository<DiscussionPoint, Long> {
    @Query(value = "{call MOD_MM_SP_GetDisucssionPointByMeetingId(:meetingId)}", nativeQuery = true)
    ArrayList<DiscussionPoint> getDiscussionPoints(@Param("meetingId") long meetingId);

}
