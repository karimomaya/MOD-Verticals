package com.mod.rest.repository;

import com.mod.rest.model.MediaMonitoringDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface MediaMonitoringDIARepository extends GenericRepository<MediaMonitoringDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_latestNews MM INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToLatestNews MMR on MMR.displayToLatestNews_to_latestNews_Id= MM.ID where MMR.displayToLatestNews_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<MediaMonitoringDIA> getMediaMonitoringDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
