package com.mod.rest.repository;

import com.mod.rest.model.LegalDocumentsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface LegalDocumentsDIARepository extends GenericRepository<LegalDocumentsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_cooperationDocument LD INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToLegalDocuments LDR on LDR.displayToLegalDocuments_to_cooperationDoc_Id= LD.ID where LDR.displayToLegalDocuments_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<LegalDocumentsDIA> getLegalDocumentsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
