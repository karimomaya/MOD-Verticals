package com.mod.rest.repository;

import com.mod.rest.dto.HumanAidDto;
import com.mod.rest.model.HumanAidDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface HumanAidDIARepository extends GenericRepository<HumanAidDIA, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedHumanAid(:PageNumber,:PageSize,:type,:countryDisplayFileId)}", nativeQuery = true)
    List<HumanAidDto> getHumanAidDIAByType( @Param("PageNumber") Integer PageNumber,
                                            @Param("PageSize") Integer PageSize,
                                            @Param("type") Integer type,
                                            @Param("countryDisplayFileId") Long countryDisplayFileId);
}
