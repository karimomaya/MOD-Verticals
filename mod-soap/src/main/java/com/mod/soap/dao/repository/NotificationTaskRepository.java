package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.NotificationTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by omar.sabry on 9/6/2020.
 */
public interface NotificationTaskRepository extends GenericRepository<NotificationTask, Long> {
    @Query(value = "{call MOD_SYS_GENERALMOD_SP_getTaskByInstanceId(:taskInstanceId)}", nativeQuery = true)
    NotificationTask getTaskByInstanceId(@Param("taskInstanceId") String taskInstanceId);
}
