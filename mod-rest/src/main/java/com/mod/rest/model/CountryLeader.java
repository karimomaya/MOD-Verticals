package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_leader")

public class CountryLeader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String picture;
    String leaderName;
    Long leaderId;
    String generalInformation;
    String leaderPosition;

}
