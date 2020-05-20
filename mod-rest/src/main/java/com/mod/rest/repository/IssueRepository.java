package com.mod.rest.repository;

import com.mod.rest.model.Issue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IssueRepository extends GenericRepository<Issue, Long> {
    @Query(value = "{call MOD_RM_SP_GetIssueNames(:PageNumber,:PageSize, :createdBy, :name )}", nativeQuery = true)
    List<Issue> getIssueByName(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("name") String name, @Param("createdBy") long createdBy);


    @Query(value = "{call MOD_RM_SP_GetDelayedIssues(:PageNumber,:PageSize, :createdBy )}", nativeQuery = true)
    List<Issue> getDelayedIssues(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedIssuesCount( :createdBy )}", nativeQuery = true)
    Long getDelayedIssuesCount(@Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetIssuesRelatedToProjectReport(:PageNumber,:PageSize, :projectId , :createdBy)}", nativeQuery = true)
    List<Issue> getIssueRelatedToProjectReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("projectId") String projectId, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetIssuesRelatedToProjectReportCount( :projectId, :createdBy )}", nativeQuery = true)
    Long getIssueRelatedToProjectReportCount(@Param("projectId") String projectId, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_GetClosedIssuesReport(:PageNumber, :PageSize, :createdBy)}", nativeQuery = true)
    List<Issue> getClosedIssuesReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_GetClosedIssuesReportCount( :createdBy )}", nativeQuery = true)
    Long getClosedIssuesReportCount(@Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetIssuesByPriorityThenPrecedence(:createdBy, :PageNumber,:PageSize )}", nativeQuery = true)
    List<Issue> getIssuesByPriorityThenPrecedence(@Param("createdBy") String createdBy, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

    @Query(value = "{call MOD_RM_SP_GetIssuesByPriorityThenPrecedenceCount( :createdBy )}", nativeQuery = true)
    Long getIssuesByPriorityThenPrecedenceCount(@Param("createdBy") String createdBy);



}


