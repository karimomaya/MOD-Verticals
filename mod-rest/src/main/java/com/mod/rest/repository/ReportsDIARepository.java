package com.mod.rest.repository;

import com.mod.rest.model.ReportsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface ReportsDIARepository extends GenericRepository<ReportsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_report RPT INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToReport RPTR on RPTR.displayToReport_to_report_Id= RPT.ID where RPTR.displayToReport_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<ReportsDIA> getReportsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
