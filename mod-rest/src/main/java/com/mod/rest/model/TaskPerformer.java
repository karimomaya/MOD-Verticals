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
@Table(name = "O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer")
public class TaskPerformer {
    @Id
    long Id;
    String type;
    /*@ManyToOne
    @JoinColumn(name = "taskPerformerToTask_Id")
    Task task;*/
    @Column(name="taskPerformerToTask_Id")
    long taskId;

    int status;
    String displayName;
    int performerId;
    Integer progress;


}
