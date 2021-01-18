package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_personConcernedCF")

public class RelatedPeopleDIA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String email;
    String name;
    String notes;
    String phone;
    String place;
    String position;

    String personConcerned_to_countryFile_Id;
}
