package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_committeeMeeting")
public class MeetingsJointCommitteeDIA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String event;
    String meetingNumber;
    String place;
    String notes;
    Date date;
    Long committeeMeeting_to_jointCommittee_Id;
}
