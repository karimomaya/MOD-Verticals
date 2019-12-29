package com.mod.rest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by karim.omaya on 10/29/2019.
 */
@Entity
@Table(name = "O2MyCompanyTaskManagementMOD_TM_entity_Program")
public class Program {
    @Id
    long Id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    Integer progress;
    String notes;
    int createdBy;
    int owner;
    int status;
    int createdByUnitId;
}
