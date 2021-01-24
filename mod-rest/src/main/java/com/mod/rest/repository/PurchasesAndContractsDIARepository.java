package com.mod.rest.repository;

import com.mod.rest.model.PurchasesAndContractsDIA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
public interface PurchasesAndContractsDIARepository extends GenericRepository<PurchasesAndContractsDIA, Long> {
    @Query(value = "SELECT * FROM O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_purchasesAndContracts PC INNER JOIN O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToPurchasesAndContracts PCR on PCR.displayToPurchasesAndContracts_to_purchasesAndContracts_Id= PC.ID where PCR.displayToPurchasesAndContracts_to_countryDisplayFile_Id = :displayFileId", nativeQuery = true)
    List<PurchasesAndContractsDIA> getPurchasesAndContractsDIAByCountryDisplayFileId(@Param("displayFileId") long displayFileId);

}
