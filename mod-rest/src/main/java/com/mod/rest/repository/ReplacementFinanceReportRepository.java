package com.mod.rest.repository;

import com.mod.rest.model.ReplacementFinanceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 7/21/2020.
 */
public interface ReplacementFinanceReportRepository extends GenericRepository<ReplacementFinanceReport, Long> {
    @Query(value = "{call MOD_IS_SP_getReplacementReportByDateRangeAndUnitCode(:startDate, :endDate, :unitCode)}", nativeQuery = true)
    List<ReplacementFinanceReport> getReplacementReportByDateRangeAndUnitCode(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("unitCode") String unitCode);
}
