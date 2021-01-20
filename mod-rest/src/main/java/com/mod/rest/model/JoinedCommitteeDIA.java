package com.mod.rest.model;

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

    @OneToMany
    @JoinColumn(name = "activityJointCommittee_to_jointCommittee_Id")
    List<ActivityJointCommittee> activityJointCommittees;

    @OneToMany
    @JoinColumn(name = "committeeMeeting_to_jointCommittee_Id")
    List<MeetingsJointCommitteeDIA> meetingsJointCommittees;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalData;


}
