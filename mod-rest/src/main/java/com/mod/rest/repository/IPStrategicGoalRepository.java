package com.mod.rest.repository;

import com.mod.rest.model.IPStrategicGoal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by abdallah.shaaban on 7/6/2020.
 */
public interface IPStrategicGoalRepository extends GenericRepository<IPStrategicGoal, Long>{

    @Query(value = "{call MOD_IED_SP_getStrategicIndicatorAndStrategicGoalOfRoadMapByUnitCode(:roadMapId, :unitCode,:halfOfYear ,:year)}", nativeQuery = true)
    ArrayList<IPStrategicGoal> getIPStrategicc(@Param("roadMapId") String roadMapId, @Param("unitCode") String unitCode, @Param("halfOfYear") String halfOfYear, @Param("year") String year);




}
