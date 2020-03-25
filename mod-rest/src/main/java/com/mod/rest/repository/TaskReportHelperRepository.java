package com.mod.rest.repository;

import com.mod.rest.model.Task;
import com.mod.rest.model.TaskReportHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
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



    @Query(value = "{call MOD_TM_SP_DelayedTaskReportProject(:userIds, :userId, :startDate, :endDate, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedTaskReportProject(:userIds, :userId, :startDate, :endDate, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getFinishedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_TM_SP_FinishedAndDelayedTaskReportProject(:userIds, :userId, :startDate, :endDate, :projectId, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getFinishedAndDelayedTaskReportProject(@Param("userIds") String userIds, @Param("userId") long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("projectId") String projectId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);


    @Query(value = "{call MOD_TM_SP_GetTaskByAssignmentReport(:startDate, :endDate, :assignmentType, :PageNumber,:PageSize)}", nativeQuery = true)
    List<TaskReportHelper> getTaskAssignmentReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("assignmentType") int assignmentType, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);


    @Query(value = "{call MOD_RM_SP_getTasksRelatedToDiscussionPointAndMeeting( :meetingId, :discussionPointId )}", nativeQuery = true)
    ArrayList<TaskReportHelper> getTasksRelatedToDiscussionPointAndMeeting(@Param("meetingId") long meetingId, @Param("discussionPointId") long discussionPointId);

    @Query(value = "{call MOD_RM_SP_GetDelayedTaskRiskReport(:PageNumber,:PageSize, :integrationIds , :createdBy)}", nativeQuery = true)
    List<TaskReportHelper> getDelayedTaskRiskReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("integrationIds") String integrationIds , @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedTaskRiskReportCount( :integrationIds, :createdBy )}", nativeQuery = true)
    Long getDelayedTaskRiskReportCount( @Param("integrationIds") String integrationIds, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetInProgressDelayedClosedTaskRisksReport(:PageNumber,:PageSize, :integrationIds , :createdBy)}", nativeQuery = true)
    List<TaskReportHelper> getInProgressDelayedClosedTaskRisksReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("integrationIds") String integrationIds , @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetInProgressDelayedClosedTaskRisksReportCount( :integrationIds, :createdBy )}", nativeQuery = true)
    Long getInProgressDelayedClosedTaskRisksReportCount( @Param("integrationIds") String integrationIds, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedTaskIssueReport(:PageNumber,:PageSize, :integrationIds , :createdBy)}", nativeQuery = true)
    List<TaskReportHelper> getDelayedTaskIssueReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("integrationIds") String integrationIds , @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedTaskIssueReportCount( :integrationIds, :createdBy )}", nativeQuery = true)
    Long getDelayedTaskIssueReportCount( @Param("integrationIds") String integrationIds, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetInProgressDelayedClosedTaskIssuesReport(:PageNumber,:PageSize, :integrationIds , :createdBy)}", nativeQuery = true)
    List<TaskReportHelper> getInProgressDelayedClosedTaskIssuesReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("integrationIds") String integrationIds , @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetInProgressDelayedClosedTaskIsuesReportCount( :integrationIds, :createdBy )}", nativeQuery = true)
    Long getInProgressDelayedClosedTaskIssuesReportCount( @Param("integrationIds") String integrationIds, @Param("createdBy") String createdBy);

}
