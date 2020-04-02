package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.ExternalUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by omaradl on 4/2/2020.
 */
public interface ExternalUserRepository extends GenericRepository<ExternalUser, Long>  {
    @Query(value = "{call MOD_CT_SP_individual_GetById(:Id)}", nativeQuery = true)
    Optional<ExternalUser> getExternalUserDetail(@Param("Id") long Id);
}
