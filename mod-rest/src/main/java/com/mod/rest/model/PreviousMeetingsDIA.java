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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_previousMeeting")
public class PreviousMeetingsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String degreeOfConfidentiality;
    String futureActions;
    String meetingTitle;
    String notes;
    String place;
    String visitTitle;
    String recommendationsAndResults;
    Date date;
    long previousMeeting_to_countryFileBasic_id;

    @OneToMany
    @JoinColumn(name = "meetingResult_to_previousMeeting_Id")
    List<MeetingsResultsDIA> meetingsResultsDIA;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalDataDIA;


}
