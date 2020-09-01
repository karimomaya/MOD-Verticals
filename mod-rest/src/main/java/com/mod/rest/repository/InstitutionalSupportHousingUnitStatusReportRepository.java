package com.mod.rest.repository;

import com.mod.rest.model.InstitutionalSupportHousingUnitStatusReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abdallah.shaaban on 9/1/2020.
 */
public interface InstitutionalSupportHousingUnitStatusReportRepository extends GenericRepository<InstitutionalSupportHousingUnitStatusReport, Long>{
    @Query(value = "{call MOD_IS_SP_housingUnitsStatusReport(:startDate, :endDate, :flatStatus, :PageNumber, :PageSize, :inputSearch)}", nativeQuery = true)
    ArrayList<InstitutionalSupportHousingUnitStatusReport> getInstitutionalSupportHousingUnitStatusReport(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("flatStatus") String flatStatus,
                                                                                           @Param("PageNumber") int PageNumber, @Param("PageSize") int PageSize, @Param("inputSearch") String inputSearch);
}
