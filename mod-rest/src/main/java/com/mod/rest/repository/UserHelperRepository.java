package com.mod.rest.repository;

import com.mod.rest.model.UserHelper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by karim.omaya on 12/24/2019.
 */
public interface UserHelperRepository extends GenericRepository<UserHelper,Long>  {
    @Query(value = "{call MOD_SYS_OC_SP_GetUserDetails(:userCN)}", nativeQuery = true)
    List<UserHelper> getUserDetail(@Param("userCN") String userCN);
    @Query(value = "{call MOD_TM_SP_task_get_subusers_of_user_without_input(:userId,  :PageNumber, :PageSize)}", nativeQuery = true)
    List<UserHelper> getSubUsers(@Param("userId") long userId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize );

}
