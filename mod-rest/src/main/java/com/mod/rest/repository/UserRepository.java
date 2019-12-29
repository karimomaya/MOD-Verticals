package com.mod.rest.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.mod.rest.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by karim.omaya on 12/22/2019.
 */
public interface UserRepository extends GenericRepository<User,Long> {
    @Query(value = "{call MOD_TM_SP_task_get_subusers_of_user(:userId, :input, :PageNumber, :PageSize)}", nativeQuery = true)
    List<User> getUserProductivityReport(@Param("userId") long userId, @Param("input") String input, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize);

}
