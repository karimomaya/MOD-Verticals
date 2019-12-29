package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.Config;

import java.util.List;

/**
 * Created by karim.omaya on 12/15/2019.
 */
public interface ConfigRepository extends GenericRepository<Config, Long>  {
    List<Config> findDistinctByCategory(String category);
}
