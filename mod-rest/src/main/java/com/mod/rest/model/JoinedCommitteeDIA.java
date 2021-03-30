package com.mod.rest.model;

import com.mod.rest.dto.ActivityJointCommitteeDto;
import com.mod.rest.dto.CountryAdditionalDto;
import com.mod.rest.dto.MeetingJointCommitteeDto;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_jointCommittee")
public class JoinedCommitteeDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String committeeName;
    String committeeNumber;
    String committeeType;
    String countryName;
    String notes;
    String jointCommittee_to_countryFileBasic_Id;
//
//    @OneToMany
//    @JoinColumn(name = "activityJointCommittee_to_jointCommittee_Id")
    @Transient
    List<ActivityJointCommitteeDto> activityJointCommittees;

//    @OneToMany
//    @JoinColumn(name = "committeeMeeting_to_jointCommittee_Id")
    @Transient
    List<MeetingJointCommitteeDto> meetingsJointCommittees;
//
//    @OneToMany
//    @JoinColumn(name = "parentEntityId")
    @Transient
    List<CountryAdditionalDto> countryAdditionalData;


}