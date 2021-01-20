package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_troopsSituation")
public class TroopsSituationDIA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String notes;
    Integer hardware;
    Integer numberOfAircraft;
    Integer numberOfVessels;
    Integer numOfCivillians;
    Integer numOfNoncommissionedOfficers;
    Integer numOfOfficers;
    Integer totalTroops;
    String campName;
    Long troopsSituation_to_troopsDoc_Id;
}
