package com.mod.rest.repository;

import com.mod.rest.model.CandidateDataReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 5/28/2020.
 */
public interface CandidateDataReportRepository extends GenericRepository<CandidateDataReport, Long> {
    @Query(value = "{call MOD_DHR_SP_getCandidateReportByDateRange(:dateForm, :dateTo)}", nativeQuery = true)
    List<CandidateDataReport> getCandidateDataReport(@Param("dateForm") String endDate, @Param("dateTo") String enDate);
}
