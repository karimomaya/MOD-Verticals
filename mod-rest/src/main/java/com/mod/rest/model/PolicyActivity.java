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
@Table(name = "O2MyCompanyPolicyManagementMOD_PM_entity_Activities")
public class PolicyActivity {
    @Id
    long Id;
    String activity_number;
    String activity_name;
    Date activity_start_date;
    Date activity_end_date;
    String activity_description;
    String activity_notes;
    Integer activity_progress_bar;
}
