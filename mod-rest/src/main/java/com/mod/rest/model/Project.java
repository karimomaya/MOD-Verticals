package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by karim.omaya on 10/29/2019.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyTaskManagementMOD_TM_entity_TaskProject")
public class Project {
    @Id
    long Id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    long owner;
    String notes;
    @ManyToOne
    @JoinColumn(name = "programId")
    Program program;
    int createdByUnitId;
    long createdBy;
    int status;
    int institutionalPlan;
    int assignToUnitId;
    Integer progress;
    @Transient
    User userOwner;
}
