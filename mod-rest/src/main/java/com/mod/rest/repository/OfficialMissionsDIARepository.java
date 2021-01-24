package com.mod.rest.repository;

import com.mod.rest.model.OfficialMissionsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface OfficialMissionsDIARepository extends GenericRepository<OfficialMissionsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_officialMissions OM INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToOfficialMissions OMR on OMR.displayToOfficialMissions_to_officialMissions_Id= OM.ID where OMR.displayToOfficialMissions_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<OfficialMissionsDIA> getOfficialMissionsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
