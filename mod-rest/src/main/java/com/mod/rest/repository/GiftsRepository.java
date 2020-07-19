package com.mod.rest.repository;

import com.mod.rest.model.Gifts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GiftsRepository extends GenericRepository<Gifts, Long> {
    @Query(value = "{call MOD_MSM_SP_giftRecordReport(:startDate, :endDate)}", nativeQuery = true)
    List<Gifts> getGiftsRecordsBetweenPurshaseDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
