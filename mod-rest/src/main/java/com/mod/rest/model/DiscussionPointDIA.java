package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_discussionPoint")

public class DiscussionPointDIA {

    //    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "Id")
    @Id
    long id;
    String field;
    String suggestedBy;
    String suggestedByValue;
    String notes;
    String discussionSubject;
    String discussionPoint_to_countryDisplayFile_Id;

//    @Transient
//    List<Test> tests;

}
