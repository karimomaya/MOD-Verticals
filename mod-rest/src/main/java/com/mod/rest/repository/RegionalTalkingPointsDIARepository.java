package com.mod.rest.repository;

import com.mod.rest.model.AspectsOfCooperationDIA;
import com.mod.rest.model.RegionalTalkingPointsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface RegionalTalkingPointsDIARepository extends GenericRepository<RegionalTalkingPointsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_regionalTalkingPoints RD INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToRegionalDiscussionPoints\n" +
            " RDR on RDR.displayToRegionalDiscussionPoints_to_regionalDiscussionPoints_Id= RD.ID where RDR.displayToRegionalDiscussionPoints_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<RegionalTalkingPointsDIA> getRegionalTalkingPointsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
