package com.mod.rest.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@NoRepositoryBean
public interface GenericRepository<T,IDT> extends PagingAndSortingRepository<T,IDT> {
}
