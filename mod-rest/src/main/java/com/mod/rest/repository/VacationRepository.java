package com.mod.rest.repository;

import com.mod.rest.model.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by omar.sabry on 12/9/2020.
 */
public interface VacationRepository extends GenericRepository<Vacation,Long> {
    @Query(value = "{call MOD_MM_SP_GetVacationsByDate(:startDate, :endDate)}", nativeQuery = true)
    List<Vacation> getByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
