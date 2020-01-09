package com.mod.rest.repository;

import com.mod.rest.model.EntityReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by omar.sabry on 1/9/2020.
 */
public interface EntityRepository extends GenericRepository<EntityReport, Long> {
    @Query(value = "{call MOD_CT_SP_entity_GetEntitiesByType(:PageNumber, :PageSize, :sortBy, :sortDir,:type, :nameArabic, :nameEnglish, :phone, :tags)}", nativeQuery = true)
    List<EntityReport> getEntitiesByType(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("sortBy") String sortBy, @Param("sortDir") String sortDir, @Param("type") int type
            , @Param("nameArabic") String nameArabic, @Param("nameEnglish") String nameEnglish, @Param("phone") String phone, @Param("tags") String tags);

    @Query(value = "{call MOD_CT_SP_entity_GetPrivateEntities(:PageNumber, :PageSize, :sortBy, :sortDir, :nameArabic, :nameEnglish, :phone, :isRegistered, :licenseNumber, :tags)}", nativeQuery = true)
    List<EntityReport> getPrivateEntities(@Param("PageNumber") int pageNumber, @Param("PageSize") int pageSize, @Param("sortBy") String sortBy, @Param("sortDir") String sortDir,
             @Param("nameArabic") String nameArabic, @Param("nameEnglish") String nameEnglish, @Param("phone") String phone, @Param("isRegistered") int isRegistered, @Param("licenseNumber") String licenseNumber, @Param("tags") String tags);
}
