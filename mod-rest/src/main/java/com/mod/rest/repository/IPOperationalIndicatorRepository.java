package com.mod.rest.repository;

import com.mod.rest.model.IPOperationalIndicatorReport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by abdallah.shaaban on 7/26/2020.
 */
public interface IPOperationalIndicatorRepository extends GenericRepository<IPOperationalIndicatorReport, Long> {
    @Query(value = "{call MOD_IED_SP_getOperationalIndicatorOfInstitutionalPlanByUnitCode(:institutionalPlanId,:unitCode,:quarter,0,5000)}", nativeQuery = true)
    ArrayList<IPOperationalIndicatorReport> getOperationalIndicatorOfInstitutionalPlanByUnitCode(@Param("institutionalPlanId") String institutionalPlanId,
                                                                                           @Param("unitCode") String unitCode, @Param("quarter") String quarter);

}
