package com.mod.rest.repository;

import com.mod.rest.model.CountryDisplay;
import com.mod.rest.repository.GenericRepository;

import java.util.Optional;

public interface CountryDisplayRepository extends GenericRepository<CountryDisplay, Long> {

    Optional<CountryDisplay> findById(Long id);
}
