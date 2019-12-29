package com.mod.rest.repository;

import com.mod.rest.model.Risk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Repository
public interface RiskRepository extends GenericRepository<Risk, Long> {
    @Query(value = "{call MOD_RM_SP_getRiskByName(:PageNumber,:PageSize, :createdBy, :name )}", nativeQuery = true)
    List<Risk> getRiskByName(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("name") String name, @Param("createdBy") long createdBy);

}
