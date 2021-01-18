package com.mod.rest.repository;

import com.mod.rest.model.JoinedCommitteeDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface JointCommitteeDIARepository extends GenericRepository<JoinedCommitteeDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_jointCommittee JC INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToJointCommittee JCR on JCR.displayToJointCommittee_to_jointCommittee_Id= JC.ID where JCR.displayToJointCommittee_to_displayFile_Id = :displayFileId", nativeQuery = true)
    List<JoinedCommitteeDIA> getJointCommitteeDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
