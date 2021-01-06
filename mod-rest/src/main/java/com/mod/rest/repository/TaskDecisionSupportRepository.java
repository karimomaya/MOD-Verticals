package com.mod.rest.repository;

import com.mod.rest.model.TaskDecisionSupport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TaskDecisionSupportRepository  extends GenericRepository<TaskDecisionSupport,Long> {

    @Query(value = "{call MOD_TM_SP_task_GetCreatedTaskByEntityItemId(:PageNumber, :PageSize, :sortBy, :sortDir, :Owner, :entityItemId)}", nativeQuery = true)
    List<TaskDecisionSupport> getCreatedTaskByEntityItemId(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize,
                                      @Param("sortBy") String sortBy, @Param("sortDir") String sortDir,
                                      @Param("Owner") String owner, @Param("entityItemId") String entityItemId);
}
