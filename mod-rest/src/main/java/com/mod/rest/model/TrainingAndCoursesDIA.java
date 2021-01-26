package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_courseAndExercise")
public class TrainingAndCoursesDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String eventName;
    String eventType;
    Date eventDate;
    String notes;
    String participatingParties;
    String sequence;
    long coursesAndExercise_to_countryFile_Id;

//    @OneToMany
//    @JoinColumn(name = "parentEntityId")
//    List<CountryAdditionalData> countryAdditionalData;

}
