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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_cooperationDocument")
public class LegalDocumentsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String documentType;
    String title;
    String notes;
    String synopsis;
    Date startDate;
    Date endDate;
    long cooperationDocument_to_countryFileBasic_Id;
}
