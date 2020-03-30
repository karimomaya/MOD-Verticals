package com.mod.rest.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryFileBasic")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String countryLogoImage;
    String flagImage;
    String mapImg;
    String MODLogoImg;
    String countryName;
    String armedforcesLogo;
}
