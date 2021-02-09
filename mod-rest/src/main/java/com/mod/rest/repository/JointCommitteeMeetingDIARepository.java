package com.mod.rest.repository;

import com.mod.rest.dto.MeetingJointCommitteeDto;
import com.mod.rest.dto.PreviousMeetingsDto;
import com.mod.rest.model.MeetingsJointCommitteeDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JointCommitteeMeetingDIARepository extends GenericRepository<MeetingsJointCommitteeDIA, Long> {

    @Query(value = "{call MOD_DIA_SP_getSelectedCommitteeMeeting(:countryDisplayFileId, :jointCommitteeId)}", nativeQuery = true)
    List<MeetingJointCommitteeDto> getSelectedCommitteeMeeting(@Param("countryDisplayFileId") Long countryDisplayFileId,
                                                               @Param("jointCommitteeId") Long jointCommitteeId);

}
