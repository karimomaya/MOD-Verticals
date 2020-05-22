package com.mod.rest.repository;

import com.mod.rest.model.TechnicalSupportReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by omar.sabry on 3/10/2020.
 */
public interface TechnicalSupportRepository extends GenericRepository<TechnicalSupportReport, Long> {
    @Query(value = "{call MOD_DHR_SP_support_getTechnicalSupportStatistics(:startDate, :endDate)}", nativeQuery = true)
    List<TechnicalSupportReport> getTechnicalSupportStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
