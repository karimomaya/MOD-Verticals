package com.mod.rest.repository;

import com.mod.rest.model.PoliticalAdviceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */
public interface PoliticalAdviceReportRepository extends GenericRepository<PoliticalAdviceReport, Long> {
    @Query(value = "{call MOD_NAD_SP_politicalAdvice_getDataByDate(:Start_Date, :End_Date, :unitTypeCode, :unitName, :unitCode)}", nativeQuery = true)
    List<PoliticalAdviceReport> getPoliticalAdviceReport(@Param("Start_Date") String startDate, @Param("End_Date") String endDate, @Param("unitTypeCode") String unitTypeCode, @Param("unitName") String unitName, @Param("unitCode") String unitCode);
}
