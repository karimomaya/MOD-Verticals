package com.mod.rest.repository;

import com.mod.rest.model.AttendanceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinaSamir on 6/16/2020.
 */
public interface AttendanceReportRepository extends GenericRepository<AttendanceReport, Long> {
    @Query(value = "SELECT * FROM awdb.dbo.O2MyCompanyDirectorateofHumanResourcesDHRMOD_DHR_entity_daily_attendance WHERE date = :date", nativeQuery = true)
    List<AttendanceReport> getAttendanceReportByDate(@Param("date") String date);
}
