package com.mod.rest.repository;

import com.mod.rest.model.CarSummary;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarSummaryRepository extends GenericRepository<CarSummary, Long>{
    @Query(value = "{call MOD_IS_SP_statistical_summary_carRecords()}", nativeQuery = true)
    List<CarSummary> getCarSummary();

}
