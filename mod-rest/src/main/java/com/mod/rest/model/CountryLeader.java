package com.mod.rest.model;

import com.mod.rest.dto.CountryAdditionalDto;
import com.mod.rest.dto.LeaderPositionDto;
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
//    المناصب
//    @OneToMany
//    @JoinColumn(name = "position_to_leader_Id")
    @Transient
    List<LeaderPositionDto> countryLeaderPositions;


//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_displayToAdditionalData",
//            joinColumns = {@JoinColumn(name = "parentEntityId")},
//            inverseJoinColumns = {@JoinColumn(name = "displayToLeaderAdditionalData_to_addtionalData_Id")}
//    )
//    @OneToMany
//    @JoinColumn(name = "parentEntityId")

    @Transient
    List<CountryAdditionalDto> countryAdditionalDatas;
}
