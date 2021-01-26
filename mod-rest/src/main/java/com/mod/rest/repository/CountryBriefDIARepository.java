package com.mod.rest.repository;

import com.mod.rest.dto.DiscussionPointsDto;
import com.mod.rest.dto.HumanAidDto;
import com.mod.rest.model.DiscussionPointDIA;
import com.mod.rest.model.HumanAidDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface CountryBriefDIARepository extends GenericRepository<DiscussionPointDIA, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedStuckedPoints(:PageNumber,:PageSize,:type,:countryDisplayFileId, :previousMeetingId)}", nativeQuery = true)
    List<DiscussionPointsDto> getSelectedStuckedPoints(@Param("PageNumber") Integer PageNumber,
                                                       @Param("PageSize") Integer PageSize,
                                                       @Param("type") Integer type,
                                                       @Param("countryDisplayFileId") Long countryDisplayFileId,
                                                       @Param("previousMeetingId") Long previousMeetingId);
}
