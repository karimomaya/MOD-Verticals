package com.mod.rest.repository;

import com.mod.rest.model.ProofreadingReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinaSamir on 5/26/2020.
 */
public interface ProofreadingReportRepository extends GenericRepository<ProofreadingReport, Long> {
    @Query(value = "{call MOD_SFA_SP_proofreading_getDataByDate(:Start_Date, :End_Date)}", nativeQuery = true)
    List<ProofreadingReport> getProofreadingReport(@Param("Start_Date") String startDate, @Param("End_Date") String endDate);
}
