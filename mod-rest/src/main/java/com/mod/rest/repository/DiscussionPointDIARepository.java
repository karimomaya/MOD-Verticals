package com.mod.rest.repository;

import com.mod.rest.dto.DiscussionPointsDto;
import com.mod.rest.model.DiscussionPointDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscussionPointDIARepository extends GenericRepository<DiscussionPointDIA, Long> {
    @Query(value = "SELECT *, lookups.ar_value as suggestedByValue FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_discussionPoint DP inner join O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_lookup as lookups on DP.suggestedBy = lookups.[stringkey] AND lookups.category = 'countryLookup' where DP.discussionPoint_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<DiscussionPointDIA> getDiscussionPointDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

    @Query(value = "{call MOD_DIA_SP_getSelectedStuckedPoints(:PageNumber,:PageSize, :countryDisplayFileId, :previousMeetingId)}", nativeQuery = true)
    List<DiscussionPointsDto> getSelectedStuckedPoints(@Param("PageNumber") Integer PageNumber,
                                                       @Param("PageSize") Integer PageSize,
                                                       @Param("countryDisplayFileId") Long countryDisplayFileId,
                                                       @Param("previousMeetingId") Long previousMeetingId);


//    Optional<CountryDisplay> findAllBy(Long id);

}
