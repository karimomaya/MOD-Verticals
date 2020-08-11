package com.mod.rest.repository;

import com.mod.rest.model.TrainingYearlyEvaluationReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */
public interface TrainingYearlyEvaluationReportRepository extends GenericRepository<TrainingYearlyEvaluationReport, Long> {
    @Query(value = "{call MOD_DHR_SP_getYearlyEvaluationByDateRange(:dateForm, :dateTo)}", nativeQuery = true)
    List<TrainingYearlyEvaluationReport> getTrainingYearlyEvaluationReport(@Param("dateForm") String startDate, @Param("dateTo") String endDate);
}
