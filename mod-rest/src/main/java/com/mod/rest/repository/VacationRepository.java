package com.mod.rest.repository;

import com.mod.rest.model.Vacation;

import java.util.List;

/**
 * Created by omar.sabry on 12/9/2020.
 */
public interface VacationRepository extends GenericRepository<Vacation,Long> {
    List<Vacation> findByRule(String rule);
}
