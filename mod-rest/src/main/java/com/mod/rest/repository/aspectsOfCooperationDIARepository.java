package com.mod.rest.repository;

import com.mod.rest.model.AspectsOfCooperationDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface aspectsOfCooperationDIARepository extends GenericRepository<AspectsOfCooperationDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryFileAspectsOfCooperation AC1 INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToAspectsOfCooperation AC on ac.displayToAspectsOfCooperation_to_aspectsOfCooperation_Id= AC1.ID where AC.displayToAspectsOfCooperation_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<AspectsOfCooperationDIA> getAspectsOfCooperationDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
