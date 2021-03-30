package com.mod.rest.repository;

import com.mod.rest.dto.LeaderPositionDto;
import com.mod.rest.model.CountryLeader;
import com.mod.rest.model.CountryLeaderPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryLeaderRepository extends GenericRepository<CountryLeader, Long> {

    @Query(value = "{call MOD_DIA_SP_getSelectedLeadersPrint(:PageNumber,:PageSize, :countryDisplayFileId )}", nativeQuery = true)
    List<CountryLeader> getCountryLeaderByDisplayFileId(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("countryDisplayFileId") Long countryDisplayFileId);


    @Query(value = "{call MOD_DIA_SP_getSelectedLeaderPosition(:leaderId,:countryDisplayFileId)}", nativeQuery = true)
    List<LeaderPositionDto> getSelectedLeaderPosition(@Param("leaderId") Long leaderId,
                                                      @Param("countryDisplayFileId") Long countryDisplayFileId);


}