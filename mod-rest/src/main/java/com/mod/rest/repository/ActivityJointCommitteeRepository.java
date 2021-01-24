package com.mod.rest.repository;

import com.mod.rest.dto.ActivityJointCommitteeDto;
import com.mod.rest.dto.HumanAidDto;
import com.mod.rest.model.ActivityJointCommittee;
import com.mod.rest.model.CountryAdditionalData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface ActivityJointCommitteeRepository extends GenericRepository<ActivityJointCommittee, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedActivityJointCommitteeAV(:PageNumber,:PageSize,:countryDisplayFileId)}", nativeQuery = true)
    List<ActivityJointCommitteeDto> getActivityJointCommitteeByCountryFileId(@Param("PageNumber") Integer PageNumber,
                                                         @Param("PageSize") Integer PageSize,
                                                         @Param("countryDisplayFileId") Long countryDisplayFileId);

    //    List<ActivityJointCommittee> findAllByCountryValueId(Long parentEntityId);
}