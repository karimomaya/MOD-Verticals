package com.mod.rest.repository;

import com.mod.rest.model.TaskPerformerHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 1/7/2020.
 */
public interface TaskPerformerHelperRepository extends GenericRepository<TaskPerformerHelper,Long> {
    @Query(value = "{call MOD_TM_SP_GetTasksByPerformerId(:performerId, :StartDate, :EndDate)}", nativeQuery = true)
    List<TaskPerformerHelper> getTasksByPerformerId(@Param("performerId") long performerId, @Param("StartDate") Date startDate, @Param("EndDate") Date endDate);
}
