package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by omaradl on 5/18/2020.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyPolicyManagementMOD_PM_entity_MilleStones")
public class PolicyMilestone {
    @Id
    long Id;
    String mileStoneName;
    Date startDate;
    Date EndDate;
    Integer progressBar;
    String comment;
}
