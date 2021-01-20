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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_troopsDoc")
public class TroopsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String countryName;
    String issuer;
    String title;
    Date date;
    long troopsDoc_to_countryFile_Id;

    @OneToMany
    @JoinColumn(name = "troopsSituation_to_troopsDoc_Id")
    List<TroopsSituationDIA> troopsSituationDIAS;

}
