package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyRiskManagementMOD_RM_entity_Issue")
public class Issue {
    @Id

    long Id;
    String issueId;
    String issueName;
    Integer probability;
    Integer effect;
    Integer effectArea;
    Integer priority;
    Date issueStartDate;
    Date issueEndDate;
    Integer issueStatus;
    String issueDescription;
    long createdBy;
    boolean isDeleted;
    Date IssueCloseDate;
    String issueProjectdescription;
    String decision;
    Integer relatedType;
    Integer projectName;
}
