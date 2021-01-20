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
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_report")
public class ReportsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String conclusion;
    String countryCode;
    String notes;
    String reportType;
    String reportFrom;
    String reportIssuer;
    String title;
    Date date;
    long report_to_countryFileBasic_Id;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalData;

}
