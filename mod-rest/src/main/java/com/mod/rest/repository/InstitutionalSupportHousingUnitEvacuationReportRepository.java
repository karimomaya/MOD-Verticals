package com.mod.rest.repository;

import com.mod.rest.model.InstitutionalSupportHousingUnitEvacuationReport;
import com.mod.rest.model.InstitutionalSupportHousingUnitStatusReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by abdallah.shaaban on 9/1/2020.
 */
public interface InstitutionalSupportHousingUnitEvacuationReportRepository extends GenericRepository<InstitutionalSupportHousingUnitEvacuationReport, Long>{
    @Query(value = "{call MOD_IS_SP_housingUnitsEvacuationReport(:startDate, :endDate, :PageNumber, :PageSize)}", nativeQuery = true)
    ArrayList<InstitutionalSupportHousingUnitEvacuationReport> getInstitutionalSupportHousingUnitEvacuationReport(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                                                                                              @Param("PageNumber") int PageNumber, @Param("PageSize") int PageSize);
}
