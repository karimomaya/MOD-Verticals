package com.mod.rest.repository;

import com.mod.rest.model.PolicyActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by omaradl on 5/18/2020.
 */
public interface PolicyActivityRepository extends GenericRepository<PolicyActivity, Long> {
    @Query(value = "{call MOD_PM_SP_GetActivitesByMilestoneId(:milestoneId)}", nativeQuery = true)
    List<PolicyActivity> getMileStoneActivites(@Param("milestoneId") long milestoneId);
}
