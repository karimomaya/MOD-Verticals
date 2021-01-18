package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_latestNews")
public class MediaMonitoringDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String category;
    String newEventTitle;
    String notes;
    String subject;
    String synopsis;
    String UAEposition;
    Date date;
    String latestNews_to_countryFileBasic_Id;

}
