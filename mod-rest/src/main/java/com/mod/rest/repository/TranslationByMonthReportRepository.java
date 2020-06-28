package com.mod.rest.repository;

import com.mod.rest.model.TranslationByMonthReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */
public interface TranslationByMonthReportRepository extends GenericRepository<TranslationByMonthReport, Long> {
    @Query(value = "{call MOD_SFA_SP_translation_getDataByMonthOrDepartment (:Start_Date, :End_Date)}", nativeQuery = true)
    List<TranslationByMonthReport> getTranslationByMonthReport(@Param("Start_Date") String startDate, @Param("End_Date") String endDate);
}
