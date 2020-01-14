package com.mod.rest.repository;

import com.mod.rest.model.Task;
import com.mod.rest.model.TaskReportHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 1/11/2020.
 */
public interface TaskReportHelperRepository extends GenericRepository<TaskReportHelper, Long>{
    @Query(value = "{call MOD_TM_SP_GetIntegrationTaskReport(:PageNumber,:PageSize,:userIds, :integrationIds, :source)}", nativeQuery = true)
    List<TaskReportHelper> getIntegrationTaskReport(@Param("userIds") String userIds, @Param("integrationIds") String integrationIds, @Param("source") String source, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_GetDelayedTask(:UserIds, :PageNumber, :PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getDelayedTasks(@Param("UserIds") String userIds, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_UserProductivityReport(:StartDate, :EndDate, :List, :PageNumber, :PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getUserProductivityReport(@Param("StartDate") Date startDate, @Param("EndDate") Date endDate, @Param("List") String list, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_CompletedTaskReport(:userIds, :userId, :startDate, :endDate,:PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getCompletedTaskReport(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_DelayedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getFinishedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedAndDelayedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getFinishedAndDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

}
