package com.mod.rest.repository;

import com.mod.rest.model.IndividualReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by omar.sabry on 1/9/2020.
 */
public interface IndividualRepository extends GenericRepository<IndividualReport, Long>{
    @Query(value = "{call MOD_CT_SP_individual_GetAllIndividuals(:PageNumber, :PageSize, :sortBy, :sortDir, :entityName, :name, :position, :tags)}", nativeQuery = true)
    List<IndividualReport> getIndividuals(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("sortBy") String sortBy, @Param("sortDir") String sortDir
            , @Param("entityName") String entityName, @Param("name") String name, @Param("position") String position, @Param("tags") String tags);
}
