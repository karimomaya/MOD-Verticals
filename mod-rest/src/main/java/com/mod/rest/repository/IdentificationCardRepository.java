package com.mod.rest.repository;

import com.mod.rest.model.IdentificationCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by omar.sabry on 3/10/2020.
 */
public interface IdentificationCardRepository extends GenericRepository<IdentificationCard, Long> {
@Query(value = "{call MOD_GCD_SP_ReadIdentificationCard(:entityId)}", nativeQuery = true)
    Optional<IdentificationCard> getIdentificationCardData(@Param("entityId") long entityId);
}