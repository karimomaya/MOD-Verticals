package com.mod.rest.repository;

import com.mod.rest.model.GeoStrategicalEventsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface GeoStrategicalEventsDIARepository extends GenericRepository<GeoStrategicalEventsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_historicEvent GE INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToHistoricEvents GER on GER.displayToHistoricEvents_to_historicEvents_Id= GE.ID where GER.displayToHistoricEvents_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<GeoStrategicalEventsDIA> getGeoStrategicalEventsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
