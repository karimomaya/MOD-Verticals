package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryDisplayFile")
public class CountryDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String sections;
    String countryValue;
    String degreeOfConfidentiality;
}
