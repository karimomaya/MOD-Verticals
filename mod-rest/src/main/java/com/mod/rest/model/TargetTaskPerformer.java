package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by karim.omaya on 10/29/2019.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer")
public class TargetTaskPerformer {
    @Id
    long Id;
    String name;
    int progress;

    /*@ManyToOne
    @JoinColumn(name = "targetTaskPerformerToTaskPerformer_Id")
    TaskPerformer taskPerformer;*/

    @Column(name = "targetTaskPerformerToTaskPerformer_Id")
    long taskPerformerId;

    int status;
    int performerId;

}

