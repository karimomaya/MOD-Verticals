package com.mod.rest.repository;

import com.mod.rest.model.CandidateDataReportByStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 5/28/2020.
 */
public interface CandidateDataReportByStatusRepository extends GenericRepository<CandidateDataReportByStatus, Long> {
    @Query(value = "{call MOD_DHR_SP_getCandidateReportByStatus()}", nativeQuery = true)
    List<CandidateDataReportByStatus> getCandidateDataReport();
}
