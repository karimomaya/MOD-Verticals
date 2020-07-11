package com.mod.rest.repository;

import com.mod.rest.model.DiscussionPointDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscussionPointDIARepository extends GenericRepository<DiscussionPointDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_discussionPoint DP where DP.discussionPoint_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<DiscussionPointDIA> getDiscussionPointDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);


//    Optional<CountryDisplay> findAllBy(Long id);

}
