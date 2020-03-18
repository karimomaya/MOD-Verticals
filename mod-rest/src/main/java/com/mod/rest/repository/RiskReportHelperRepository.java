package com.mod.rest.repository;

import com.mod.rest.model.Risk;
import com.mod.rest.model.RiskReportHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Repository
public interface RiskReportHelperRepository extends GenericRepository<RiskReportHelper, Long> {
    @Query(value = "{call MOD_RM_SP_getRiskByName(:PageNumber,:PageSize, :createdBy, :name )}", nativeQuery = true)
    List<RiskReportHelper> getRiskByName(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("name") String name, @Param("createdBy") long createdBy);


    @Query(value = "{call MOD_RM_SP_GetDelayedRisks(:PageNumber,:PageSize, :createdBy )}", nativeQuery = true)
    List<RiskReportHelper> getDelayedRisks(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedRisksCount( :createdBy )}", nativeQuery = true)
    Long getDelayedRisksCount(@Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetRisksRelatedToProjectReport(:PageNumber,:PageSize, :projectId , :createdBy)}", nativeQuery = true)
    List<RiskReportHelper> getRiskRelatedToProjectReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("projectId") String projectId, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetRisksRelatedToProjectReportCount( :projectId, :createdBy )}", nativeQuery = true)
    Long getRiskRelatedToProjectReportCount(@Param("projectId") String projectId, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_GetClosedRisksReport(:PageNumber, :PageSize, :createdBy)}", nativeQuery = true)
    List<RiskReportHelper> getClosedRisksReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_GetClosedRisksReportCount( :createdBy )}", nativeQuery = true)
    Long getClosedRisksReportCount(@Param("createdBy") String createdBy);

}


