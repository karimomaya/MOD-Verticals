package com.mod.rest.repository;

import com.mod.rest.model.TaskPerformer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by omar.sabry on 2/27/2020.
 */
public interface TaskPerformerRepository  extends GenericRepository<TaskPerformer,Long> {
    @Query(value = "{call MOD_TM_SP_task_GetListOfWorkingUser(:PageNumber, :PageSize, :taskId)}", nativeQuery = true)
    ArrayList<TaskPerformer> getPerformersByTaskId(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("taskId") long taskId);
}