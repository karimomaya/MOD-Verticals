package com.mod.rest.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

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
    String territory;
    String officialLanguage;
    String capital;
    String area;
    String countryCode;
    String numberOfCommunity;
    String relationshiphLevel;
    String currency;
    String ethnicGroups;
    @Transient
    String degreeOfConf;

    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public String getCountryCoverName(){
        return "ملف التعاون مع "+ this.countryName;
    }



}
