package com.mod.rest.repository;

import com.mod.rest.model.CarGeneralReport;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface CarGeneralReportRepository extends GenericRepository<CarGeneralReport,Long>{
    List<CarGeneralReport> findByExpiryDateOfTheLicenseBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
