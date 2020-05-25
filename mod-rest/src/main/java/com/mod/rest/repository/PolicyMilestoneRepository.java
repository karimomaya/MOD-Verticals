package com.mod.rest.repository;

import com.mod.rest.model.PolicyMilestone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by omaradl on 5/18/2020.
 */
public interface PolicyMilestoneRepository extends GenericRepository<PolicyMilestone, Long> {
    @Query(value = "{call MOD_PM_SP_GetMilestonesByWorkPlanId(:workplanId)}", nativeQuery = true)
    List<PolicyMilestone> getWorkplanMilestones(@Param("workplanId") long workplanId);
}
