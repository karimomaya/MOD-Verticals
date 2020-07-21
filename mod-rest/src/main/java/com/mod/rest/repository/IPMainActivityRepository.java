package com.mod.rest.repository;

import com.mod.rest.model.IPMainActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abdallah.shaaban on 7/20/2020.
 */
public interface IPMainActivityRepository extends GenericRepository<IPMainActivity, Long>{
    @Query(value = "{call MOD_IED_SP_getMainActivitiesByDIVUnitCodesForTimeLine(:unitCodes,:institutionalPlan,:startDate,:endDate)}", nativeQuery = true)
    ArrayList<IPMainActivity> getIPMainActitvityByDIVUnitCode(@Param("unitCodes") String unitCodes, @Param("institutionalPlan") String institutionalPlan,
                                                              @Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
