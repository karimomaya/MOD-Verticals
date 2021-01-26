package com.mod.rest.repository;

import com.mod.rest.dto.HistoryOfCommonRelationDto;
import com.mod.rest.model.HistoryOfCommonRelationDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface HistoryOfCommonRelationDIARepository extends GenericRepository<HistoryOfCommonRelationDIA, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedHistoryOfCommonRel (:PageNumber,:PageSize,:countryDisplayFileId)}", nativeQuery = true)
    List<HistoryOfCommonRelationDto> getSelectedHistoryOfCommonRel(@Param("PageNumber") Integer PageNumber,
                                                          @Param("PageSize") Integer PageSize,
                                                          @Param("countryDisplayFileId") Long countryDisplayFileId);
}
