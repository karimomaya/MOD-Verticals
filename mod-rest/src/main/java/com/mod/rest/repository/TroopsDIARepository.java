package com.mod.rest.repository;

import com.mod.rest.model.TroopsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface TroopsDIARepository extends GenericRepository<TroopsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_troopsDoc TS INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToTroopsDoc TSR on TSR.displayToTroopsDoc_to_troopsDoc_Id= TS.ID where TSR.displayToTroopsDoc_to_countryDisplayfile_Id = :displayFileId", nativeQuery = true)
    List<TroopsDIA> getTroopsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
