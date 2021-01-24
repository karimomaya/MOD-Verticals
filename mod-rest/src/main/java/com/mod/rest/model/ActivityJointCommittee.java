package com.mod.rest.model;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;

/**
 * Created by amira.sherif on 1/13/2021.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_activityJointCommittee")
public class ActivityJointCommittee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String activity;
    String notes;
    Date date;
    Long activityJointCommittee_to_jointCommittee_Id;
}

