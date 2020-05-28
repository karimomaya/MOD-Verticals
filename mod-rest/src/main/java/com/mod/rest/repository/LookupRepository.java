package com.mod.rest.repository;

import com.mod.rest.model.Lookup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinaSamir on 5/27/2020.
 */
public interface LookupRepository extends GenericRepository<Lookup, Long> {
    @Query(value = "SELECT * FROM awdb.dbo.O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_lookup WHERE category = :category", nativeQuery = true)
    List<Lookup> getLookupByCategory(@Param("category") String category);
}
