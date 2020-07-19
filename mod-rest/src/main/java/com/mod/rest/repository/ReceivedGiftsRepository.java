package com.mod.rest.repository;


import com.mod.rest.model.ReceivedGifts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceivedGiftsRepository extends GenericRepository<ReceivedGifts, Long> {
    @Query(value = "{call MOD_MSM_SP_receivedGiftRecordReport (:startDate, :endDate)}", nativeQuery = true)
    List<ReceivedGifts> getReceivedGiftsRecordsBetweenPurshaseDate(@Param("startDate") String startDate, @Param("endDate") String endDate);


}
