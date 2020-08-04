package com.mod.rest.repository;

import com.mod.rest.model.PolicyReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karim on 6/23/20.
 */
public interface PolicyReportRepository extends GenericRepository<PolicyReport, Long>{

    @Query(value = "{call MOD_PM_SP_getReport(:policyName,:startDate,:endDate, :PageNumber, :PageSize, :userEntityId, :userUnitId )}", nativeQuery = true)
    ArrayList<PolicyReport> getPolicyReportStatistics(@Param("policyName") String policyName, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("PageNumber") int PageNumber , @Param("PageSize") int PageSize, @Param("userEntityId") int userEntityId, @Param("userUnitId") String userUnitId);
}
