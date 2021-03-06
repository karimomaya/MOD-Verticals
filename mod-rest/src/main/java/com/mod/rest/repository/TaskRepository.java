package com.mod.rest.repository;

import com.mod.rest.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/10/2019.
 */
public interface TaskRepository extends GenericRepository<Task,Long> {
    @Query(value = "{call MOD_TM_SP_GetDelayedTaskCount(:List)}", nativeQuery = true)
    Long cDelayedTask(@Param("List") String list);
    @Query(value = "{call MOD_TM_SP_GetDelayedTask(:UserIds, :PageNumber, :PageSize)}", nativeQuery = true)
    List<Task> getDelayedTasks(@Param("UserIds") String userIds, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);
    @Query(value = "{call MOD_TM_SP_UserProductivityReportCount(:StartDate, :EndDate, :List)}", nativeQuery = true)
    Long cUserProductivityReport(@Param("StartDate") Date startDate, @Param("EndDate") Date endDate, @Param("List") String list);
    @Query(value = "{call MOD_TM_SP_UserProductivityReport(:StartDate, :EndDate, :List, :PageNumber, :PageSize)}", nativeQuery = true)
    List<Task> getUserProductivityReport(@Param("StartDate") Date startDate, @Param("EndDate") Date endDate, @Param("List") String list, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_CompletedTaskReportCount(:userIds, :userId, :startDate, :endDate)}", nativeQuery = true)
    Long cCompletedTaskReport(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query(value = "{call MOD_TM_SP_CompletedTaskReport(:userIds, :userId, :startDate, :endDate,:PageNumber,:PageSize)}", nativeQuery = true)
    List<Task> getCompletedTaskReport(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedTaskReportProjectCount(:userIds, :userId, :projectId)}", nativeQuery = true)
    Long cFinishedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId);
    @Query(value = "{call MOD_TM_SP_FinishedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<Task> getFinishedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);


    @Query(value = "{call MOD_TM_SP_DelayedTaskReportProjectCount(:userIds, :userId, :projectId)}", nativeQuery = true)
    Long cDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId);
    @Query(value = "{call MOD_TM_SP_DelayedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<Task> getDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedAndDelayedTaskReportProjectCount(:userIds, :userId, :projectId)}", nativeQuery = true)
    Long cFinishedAndDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId);
    @Query(value = "{call MOD_TM_SP_FinishedAndDelayedTaskReportProject(:userIds, :userId, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<Task> getFinishedAndDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_GetIntegrationTaskReportCount(:userIds, :integrationIds, :source)}", nativeQuery = true)
    Long cGetIntegrationTaskReport(@Param("userIds") String userIds, @Param("integrationIds") String integrationIds, @Param("source") String source);
    @Query(value = "{call MOD_TM_SP_GetIntegrationTaskReport(:PageNumber,:PageSize,:userIds, :integrationIds, :source)}", nativeQuery = true)
    List<Task> getIntegrationTaskReport(@Param("userIds") String userIds, @Param("integrationIds") String integrationIds, @Param("source") String source, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

}
