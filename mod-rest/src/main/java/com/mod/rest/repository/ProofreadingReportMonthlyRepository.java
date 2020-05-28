package com.mod.rest.repository;

import com.mod.rest.model.ProofreadingReportMonthly;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinaSamir on 5/27/2020.
 */
public interface ProofreadingReportMonthlyRepository extends GenericRepository<ProofreadingReportMonthly, Long> {
    @Query(value = "{call MOD_SFA_SP_proofreading_getDataByMonthOrDepartment(:Start_Date, :End_Date)}", nativeQuery = true)
    List<ProofreadingReportMonthly> getProofreadingReportMonthly(@Param("Start_Date") String startDate, @Param("End_Date") String endDate);
}
