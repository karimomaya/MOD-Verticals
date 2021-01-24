package com.mod.rest.repository;

import com.mod.rest.model.CooperationImportanceDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface CooperationImportanceDIARepository extends GenericRepository<CooperationImportanceDIA, Long> {
    @Query(value = "select * from O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_cooperationImportance where cooperationImportance_to_countryFileBasic_Id  = :countryFileBasicId", nativeQuery = true)
    List<CooperationImportanceDIA> getCooperationImportanceDIABycountryFileBasicId(@Param("countryFileBasicId") String displayFileId);

}
