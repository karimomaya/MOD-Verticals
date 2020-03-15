package com.mod.rest.repository;

import com.mod.rest.model.DesignSeal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by omar.sabry on 3/10/2020.
 */
public interface DesignSealRepository extends GenericRepository<DesignSeal, Long>{
    @Query(value = "{call MOD_GCD_SP_ReadDesignSeal(:entityId)}", nativeQuery = true)
    Optional<DesignSeal> getDesignSealData(@Param("entityId") long entityId);
}
