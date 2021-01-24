package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_cooperationImportance")
public class CooperationImportanceDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String economic;
    String military;
    String political;
    String notes;
    long cooperationImportance_to_countryFileBasic_Id;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalData;

}
