package com.mod.rest.repository;

import com.mod.rest.model.PolicyReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by karim on 6/23/20.
 */
public interface PolicyReportRepository extends GenericRepository<PolicyReport, Long>{
    @Query(value = "{call MOD_PM_SP_getReport(:policyName, :PageNumber, :PageSize)}", nativeQuery = true)
    List<PolicyReport> getPolicyReportStatistics(@Param("policyName") String policyName, @Param("PageNumber") int PageNumber , @Param("PageSize") int PageSize);
}
