package com.mod.rest.repository;

import com.mod.rest.dto.CountryAdditionalDto;
import com.mod.rest.model.CountryAdditionalData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface CountryAdditionalRepository extends GenericRepository<CountryAdditionalData, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedAdditionalData(:PageNumber,:PageSize, :parentEntityId, :type, :getALL, :countryDisplayFileId  )}", nativeQuery = true)
    List<CountryAdditionalDto> findAllByTypeAndParentEntityId(@Param("PageNumber") Integer PageNumber, @Param("PageSize") Integer PageSize,
                                                              @Param("parentEntityId") Long parentEntityId,
                                                              @Param("type") String type,
                                                              @Param("getALL") Integer getALL,
                                                              @Param("countryDisplayFileId") Long countryDisplayFileId);


    @Query(value = "{call MOD_DIA_SP_getSelectedLastMeetingAdditionalData(:PageNumber,:PageSize,:countryDisplayFileId, :previousMeetingId)}", nativeQuery = true)
    List<CountryAdditionalDto> getSelectedLastMeetingAdditionalData(@Param("PageNumber") Integer PageNumber,
                                                           @Param("PageSize") Integer PageSize,
                                                           @Param("countryDisplayFileId") Long countryDisplayFileId,
                                                           @Param("previousMeetingId") Long previousMeetingId);

}