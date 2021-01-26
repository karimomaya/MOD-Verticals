package com.mod.rest.repository;

import com.mod.rest.dto.PreviousMeetingsDto;
import com.mod.rest.model.JoinedCommitteeDIA;
import com.mod.rest.model.PreviousMeetingsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface PreviousMeetingsDIARepository extends GenericRepository<PreviousMeetingsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_previousMeeting PM INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToPreviousMeeting PMR on PMR.displayToPreviousMeeting_to_previousMeeting_Id= PM.ID where PMR.displayToPreviousMeeting_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<PreviousMeetingsDIA> getPreviousMeetingsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

    @Query(value = "{call MOD_DIA_SP_getLastMeeting( :displayFileId)}", nativeQuery = true)
    List<PreviousMeetingsDto> getLatestMeeting(@Param("displayFileId") Long displayFileId);

}
