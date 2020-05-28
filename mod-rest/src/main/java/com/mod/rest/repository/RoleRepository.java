package com.mod.rest.repository;

import com.mod.rest.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinaSamir on 5/27/2020.
 */
public interface RoleRepository extends GenericRepository<Role, Long> {
    @Query(value = "SELECT * FROM awdb.dbo.O2comassetegOrganizationChartMOD_SYS_OC_entity_role WHERE roleCode = :roleCode", nativeQuery = true)
    List<Role> getRoleByRoleCode(@Param("roleCode") String roleCode);

    @Query(value = "SELECT * FROM awdb.dbo.O2comassetegOrganizationChartMOD_SYS_OC_entity_role", nativeQuery = true)
    List<Role> getAllRoles();
}
