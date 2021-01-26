package com.mod.rest.repository;

import com.mod.rest.dto.HumanAidDto;
import com.mod.rest.dto.TrainingAndCoursesDto;
import com.mod.rest.model.HumanAidDIA;
import com.mod.rest.model.TrainingAndCoursesDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface TrainingAndCoursesDIARepository extends GenericRepository<TrainingAndCoursesDIA, Long> {
    @Query(value = "{call MOD_DIA_SP_getSelectedTrainingAndCourses(:PageNumber,:PageSize,:type,:countryDisplayFileId)}", nativeQuery = true)
    List<TrainingAndCoursesDto> getTrainingAndCoursesDIAByType(@Param("PageNumber") Integer PageNumber,
                                                               @Param("PageSize") Integer PageSize,
                                                               @Param("type") String type,
                                                               @Param("countryDisplayFileId") Long countryDisplayFileId);
}
