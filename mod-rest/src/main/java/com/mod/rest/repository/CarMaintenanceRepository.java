package com.mod.rest.repository;

import com.mod.rest.model.CarMaintenanceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarMaintenanceRepository extends GenericRepository <CarMaintenanceReport,Long>{
    @Query(value = "{call MOD_IS_SP_car_maintenancen_accidents_count(:year)}", nativeQuery = true)
    List<CarMaintenanceReport> getMaintenenceCountByYear(@Param("year") String year);

}
