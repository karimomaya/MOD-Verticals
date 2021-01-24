package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_purchasesAndContracts")
public class PurchasesAndContractsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String company;
    Integer contractValue;
    Date timePeriodFrom;
    Date timePeriodTo;
    String currency;
    String projectOrContract;
    String recipientUnit;
    String status;
    String notes;
    long purchasesAndContracts_to_countryFileBasic_Id;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalData;

}
