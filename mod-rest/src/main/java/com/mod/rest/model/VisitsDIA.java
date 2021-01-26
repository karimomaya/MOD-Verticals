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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_visit")
public class VisitsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String goals;
    Date date;
    String notes;
    long visit_to_countryFileBasic_Id;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalDataVisits;

}
