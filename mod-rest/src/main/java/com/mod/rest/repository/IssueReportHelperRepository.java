package com.mod.rest.repository;

import com.mod.rest.model.Issue;
import com.mod.rest.model.IssueReportHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IssueReportHelperRepository extends GenericRepository<IssueReportHelper, Long> {
    @Query(value = "{call MOD_RM_SP_GetIssueNames(:PageNumber,:PageSize, :createdBy, :name )}", nativeQuery = true)
    List<IssueReportHelper> getIssueByName(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("name") String name, @Param("createdBy") long createdBy);

    @Query(value = "{call MOD_RM_SP_GetDelayedIssues(:PageNumber,:PageSize, :createdBy )}", nativeQuery = true)
    List<IssueReportHelper> getDelayedIssues(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetIssuesRelatedToProjectReport(:PageNumber,:PageSize, :projectId , :createdBy)}", nativeQuery = true)
    List<IssueReportHelper> getIssueRelatedToProjectReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("projectId") String projectId, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_GetClosedIssuesReport(:PageNumber, :PageSize, :createdBy)}", nativeQuery = true)
    List<IssueReportHelper> getClosedIssuesReport(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("createdBy") String createdBy);

    @Query(value = "{call MOD_RM_SP_GetIssuesByPriorityThenPrecedence(:createdBy, :PageNumber,:PageSize )}", nativeQuery = true)
    List<IssueReportHelper> getIssuesByPriorityThenPrecedence(@Param("createdBy") String createdBy, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

}


