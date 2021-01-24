package com.mod.rest.repository;

import com.mod.rest.dto.VisitsDto;
import com.mod.rest.model.VisitsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface VisitsDIARepository extends GenericRepository<VisitsDIA, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedVisits(:PageNumber,:PageSize,:countryDisplayFileId)}", nativeQuery = true)
    List<VisitsDto> getSelectedVisitsDIAByCountryFileId(@Param("PageNumber") Integer PageNumber,
                                                        @Param("PageSize") Integer PageSize,
                                                        @Param("countryDisplayFileId") Long countryDisplayFileId);

    //    List<ActivityJointCommittee> findAllByCountryValueId(Long parentEntityId);
}