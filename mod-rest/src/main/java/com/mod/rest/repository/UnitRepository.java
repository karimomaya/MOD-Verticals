package com.mod.rest.repository;

import com.mod.rest.model.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by MinaSamir on 5/27/2020.
 */
public interface UnitRepository extends GenericRepository<Unit, Long> {
    @Query(value = "SELECT * FROM awdb.dbo.O2comassetegOrganizationChartMOD_SYS_OC_entity_unit WHERE UnitCode = :unitCode", nativeQuery = true)
    List<Unit> getUnitByUnitCode(@Param("unitCode") String unitCode);

    @Query(value = "SELECT * FROM awdb.dbo.O2comassetegOrganizationChartMOD_SYS_OC_entity_unit", nativeQuery = true)
    List<Unit> getAllUnits();
    @Query(value = "{call MOD_SYS_GENERAL_SP_GetUnitsUnderUnitCodeByUnitTypeCodes(:unitCode,:unitTypeCodes,0,5000)}", nativeQuery = true)
    List<Unit> getUnitsUnderUnitCodeByUnitTypeCodes(@Param("unitCode") String unitCode, @Param("unitTypeCodes") String unitTypeCodes);

}
