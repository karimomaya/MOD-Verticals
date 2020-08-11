package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_leader")

public class CountryLeader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String picture;
    String name;
    String generalInformation;
    String currentPosition;
    @OneToMany
    @JoinColumn(name = "position_to_leader_Id")
    List<CountryLeaderPosition> countryLeaderPositions;


}
