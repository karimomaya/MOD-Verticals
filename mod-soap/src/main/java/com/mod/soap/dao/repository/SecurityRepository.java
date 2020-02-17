package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.Security;

import java.util.Optional;

/**
 * Created by karim.omaya on 1/12/2020.
 */
public interface SecurityRepository extends GenericRepository<Security, Long> {

    Optional<Security> findByTarget(String target);
}
