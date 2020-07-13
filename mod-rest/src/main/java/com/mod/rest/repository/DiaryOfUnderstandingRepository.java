package com.mod.rest.repository;

import com.mod.rest.model.DiaryOfUnderstandingReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */
public interface DiaryOfUnderstandingRepository extends GenericRepository<DiaryOfUnderstandingReport, Long> {
    @Query(value = "{call MOD_SCO_annual_plan_meetings_SP_getDiariesUnderstadingByDateRange(:Start_Date, :End_Date)}", nativeQuery = true)
    List<DiaryOfUnderstandingReport> getDiaryOfUnderstandingReport(@Param("Start_Date") String startDate, @Param("End_Date") String endDate);
}
