package com.mod.rest.repository;

import com.mod.rest.model.IPSubActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by abdallah.shaaban on 7/19/2020.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IPSubActivityRepository  extends GenericRepository<IPSubActivity, Long>{
    @Query(value = "{call MOD_IED_SP_getSubActivitiesBySECUnitCodesForTimeLine(:unitCodes,:institutionalPlan,:mainActivityId,:startDate,:endDate)}", nativeQuery = true)
    ArrayList<IPSubActivity> getIPSubActivity(@Param("unitCodes") String unitCodes,@Param("institutionalPlan") String institutionalPlan,@Param("mainActivityId") Long mainActivityId,
                                              @Param("startDate") Date startDate,@Param("endDate") Date endDate);
    @Query(value = "{call MOD_IED_SP_getSubActivitiesBySECUnitCodesAndTypeOfAssignment(:unitCodes,:institutionalPlan,:startDate,:endDate,:typeOfAssignment,0,5000)}", nativeQuery = true)
    ArrayList<IPSubActivity> getSubActivitiesBySECUnitCodes(@Param("unitCodes") String unitCodes,@Param("institutionalPlan") String institutionalPlan,
                                                            @Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("typeOfAssignment") String typeOfAssignment);

}
