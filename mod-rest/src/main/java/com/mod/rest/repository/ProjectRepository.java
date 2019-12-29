package com.mod.rest.repository;

import com.mod.rest.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/21/2019.
 */
@Repository
public interface ProjectRepository extends GenericRepository<Project, Long> {
    @Query("SELECT COUNT(project) FROM Project project where project.startDate <= :endDate  and project.startDate >= :startDate and (project.createdBy = :userId or project.owner = :userId)")
    Long cProject(@Param("startDate") Date startDate, @Param("endDate")Date endDate, @Param("userId") long userId);

    @Query("SELECT project from Project project where project.startDate <= :endDate  and project.startDate >= :startDate and (project.createdBy = :userId or project.owner = :userId)")
    List<Project> getProjects(@Param("startDate") Date startDate, @Param("endDate")Date endDate, @Param("userId") long userId, Pageable pageable);

    @Query(value = "{call MOD_TM_SP_task_GetProjectByHeadUnit(:unitId, :PageNumber, :PageSize, :input, :status)}", nativeQuery = true)
    List<Project> getProjectByHeadUnit(@Param("unitId") long unitId,@Param("PageNumber") int pageNumber,@Param("PageSize") int pageSize,@Param("input") String input,@Param("status") int status );
}
