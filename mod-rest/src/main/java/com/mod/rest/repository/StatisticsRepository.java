package com.mod.rest.repository;

import com.mod.rest.model.Statistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2020.
 */
public interface StatisticsRepository extends GenericRepository<Statistics, Long> {

    @Query(value = "{call MOD_RM_SP_GetUserRiskStatisticsReport(:createdBy, :StartDate, :EndDate)}", nativeQuery = true)
    List<Statistics> getUserRiskStatisticsReport(@Param("createdBy") long createdBy, @Param("StartDate") Date StartDate, @Param("EndDate") Date EndDate);

    @Query(value = "{call MOD_RM_SP_GetUserIssueStatisticsReport(:createdBy, :StartDate, :EndDate)}", nativeQuery = true)
    List<Statistics> getUserIssueStatisticsReport(@Param("createdBy") long createdBy, @Param("StartDate") Date StartDate, @Param("EndDate") Date EndDate);

}