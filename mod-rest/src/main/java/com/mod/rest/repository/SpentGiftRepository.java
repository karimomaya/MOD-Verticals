package com.mod.rest.repository;

import com.mod.rest.model.SpentGift;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpentGiftRepository extends   GenericRepository<SpentGift, Long>{
@Query(value = "{call MOD_MSM_SpentGiftsReport (:startDate, :endDate, :receiverCounty, :receiverName)}", nativeQuery = true)
List<SpentGift> getSpentGiftsRecordsBetweenPurshaseDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("receiverCounty") String receiverCounty, @Param("receiverName") String receiverName);

}