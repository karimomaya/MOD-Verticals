package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_meetingResult")
public class MeetingsResultsDIA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    Date replyDate;
    String situation;
    String notes;
    String result;
    Long meetingResult_to_previousMeeting_Id;
}
