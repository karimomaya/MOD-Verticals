package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_historyOfCommonRelation")
public class HistoryOfCommonRelationDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String event;
    String notes;
    Date date;
    String position;
    long historyOfCommonRelation_to_countryfileBasic_Id;

}
