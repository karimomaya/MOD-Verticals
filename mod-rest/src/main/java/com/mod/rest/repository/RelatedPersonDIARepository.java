package com.mod.rest.repository;

import com.mod.rest.model.RelatedPeopleDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface RelatedPersonDIARepository extends GenericRepository<RelatedPeopleDIA, Long> {
    @Query(value = "SELECT * \n" +
            "FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_personConcernedCF RP \n" +
            "INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToRleatedPeople RPR \n" +
            "on RP.Id= RPR.displayToRelatedPeople_to_relatedPeople_Id where RPR.display_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<RelatedPeopleDIA> getRelatedPeopleDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
