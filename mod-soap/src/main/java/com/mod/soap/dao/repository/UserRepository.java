package com.mod.soap.dao.repository;

import com.mod.soap.dao.model.ExternalUser;
import com.mod.soap.dao.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by karim.omaya on 12/15/2019.
 */
public interface UserRepository extends GenericRepository<User, Long> {
    @Query(value = "{call MOD_SYS_OC_SP_GetUserDetails(:userCN)}", nativeQuery = true)
    List<User> getUserDetail(@Param("userCN") String userCN);
    @Query(value = "{call MOD_SYS_OC_SP_GetUserDetailsByUserEntityId(:userEntityId)}", nativeQuery = true)
    Optional<User> getUserDetail(@Param("userEntityId") long userEntityId);
    @Query(value = "{call MOD_TM_SP_task_get_subusers_of_user_without_input(:userId,  :PageNumber, :PageSize)}", nativeQuery = true)
    List<User> getSubUsers(@Param("userId") long userId, @Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize );
    @Query(value = "{call MOD_SYS_OC_SP_GetUserByRoleName(:RoleName)}", nativeQuery = true)
    List<User> getUsersByRoleName(@Param("RoleName") String RoleName);
    @Query(value = "{call MOD_SYS_OC_SP_GetUserDetailsByUserId(:UserId)}", nativeQuery = true)
    List<User> getUserDetailsByUserId(@Param("UserId") String UserId);
}
