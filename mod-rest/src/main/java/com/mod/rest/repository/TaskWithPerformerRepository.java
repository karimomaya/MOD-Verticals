package com.mod.rest.repository;

import com.mod.rest.model.TaskWithPerformer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by omaradl on 7/15/2020.
 */
public interface TaskWithPerformerRepository extends GenericRepository<TaskWithPerformer,Long> {
    @Query(value = "{call MOD_TM_SP_UserProductivityReportCount(:StartDate, :EndDate, :List)}", nativeQuery = true)
    Long cUserProductivityReport(@Param("StartDate") Date startDate, @Param("EndDate") Date endDate, @Param("List") String list);
    @Query(value = "{call MOD_TM_SP_UserProductivityReport(:StartDate, :EndDate, :List, :PageNumber, :PageSize)}", nativeQuery = true)
    List<TaskWithPerformer> getUserProductivityReport(@Param("StartDate") Date startDate, @Param("EndDate") Date endDate, @Param("List") String list, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);
}
