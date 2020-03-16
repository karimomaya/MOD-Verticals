package com.mod.rest.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by amira.sherif on 3/16/2020.
 */

@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyGovernmentExcellenceIEDMOD_IED_entity_mainActivity")

public class MainActivity {
    @Id
    long Id;
    String activityName;
    String activityDescription;
}
