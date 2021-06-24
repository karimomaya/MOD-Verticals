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
    int degreeOfConfidentiality;
    Long S_WORKSPACEID;
    String displayFileTitle;

    public String confidentialityDegree(){
        switch (this.degreeOfConfidentiality){
            case 1:
                return "سري للغاية";
            case 2:
                return "سري";
            case 3:
                return "محظور";
            case 4:
                return "مكتوم";
            default:
                return "";
        }
    }
}
