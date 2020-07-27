package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    Long Id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    Long owner;
    String notes;
    @ManyToOne
    @JoinColumn(name = "programId")
    Program program;
    Integer createdByUnitId;
    Long createdBy;
    Integer status;
    Integer institutionalPlan;
    Integer assignToUnitId;
    Integer progress;
    @Transient
    User userOwner;
}
