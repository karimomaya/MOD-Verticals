package com.mod.rest.repository;

import com.mod.rest.model.ReceivedCarReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceivedCarsReportRepository extends GenericRepository <ReceivedCarReport,Long>{
    @Query(value = "{call MOD_IS_SP_getReceivedCarReport(:fromDate, :toDate)}", nativeQuery = true)
    List<ReceivedCarReport> getReceivedCarsRecordBetweenReceiveDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
