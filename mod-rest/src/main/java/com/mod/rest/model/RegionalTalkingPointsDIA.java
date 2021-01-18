package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_regionalTalkingPoints")
public class RegionalTalkingPointsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String brief;
    String notes;
    String subject;
    String positionOfUAE;
    Date date;
    String regionalTalkingPoints_to_countryFileBasic_Id;

}
