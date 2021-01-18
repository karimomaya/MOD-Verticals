package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryFileAspectsOfCooperation")
public class AspectsOfCooperationDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String aspectsOfCooperation;
    String details;
    Date date;
    String aspectsOfCooperation_to_countryFileBasic_Id;

}
