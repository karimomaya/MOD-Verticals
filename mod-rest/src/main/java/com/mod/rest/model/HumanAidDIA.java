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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_humanAid")
public class HumanAidDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String assistanceProvided;
    String military;
    Date date;
    String reasonForAidOrEvent;
    String type;
    String totalValueOfAid;
    long humanAid_to_countryFile_Id;

//    @OneToMany
//    @JoinColumn(name = "parentEntityId")
//    List<CountryAdditionalData> countryAdditionalData;

}
