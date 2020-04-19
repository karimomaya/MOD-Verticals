package com.mod.rest.repository;

import com.mod.rest.model.PurchaseOrderReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by omar.sabry on 4/19/2020.
 */
public interface PurchaseOrderRepository extends GenericRepository<PurchaseOrderReport, Long> {
    @Query(value = "{call MOD_IS_SP_purchaseOrderReport(:startDate, :endDate, :entityBeneficiary, :PageNumber, :PageSize, :inputSearch)}", nativeQuery = true)
    List<PurchaseOrderReport> getPurchaseOrderReport(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("entityBeneficiary") String entityBeneficiary, @Param("PageNumber") int PageNumber, @Param("PageSize") int PageSize, @Param("inputSearch") String inputSearch);
}
