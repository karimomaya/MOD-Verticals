package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyRiskManagementMOD_RM_entity_Risk")
public class Risk {
    @Id
    long Id;

    String riskNumber;
    String riskName;
    Integer priority;
    Date riskDate;
    Date riskSolutionDate;
    Integer riskState;
    String riskDescription;
    long createdBy;
    boolean isDeleted;
    Date riskCloseDate;
   // String riskProjectdescription;
    Integer relatedType;
    Integer projectName;
}
