package com.mod.rest.model;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * Created by amira.sherif on 3/16/2020.
 */

@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyRiskManagementMOD_RM_entity_RiskResponsible")

public class RiskResponsibleHelper {
    @Id
    @Column(name="Id")
    long Id;

    @ManyToOne
    @JoinColumn(name = "RiskResponsibleToRisk_Id")
    @NotFound(action = NotFoundAction.IGNORE)
    RiskReportHelper riskId;
  //  long responsibleId;
    @ManyToOne
    @JoinColumn(name = "responsibleId")
    @NotFound(action = NotFoundAction.IGNORE)
    User responsible;
}
