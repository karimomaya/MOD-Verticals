package com.mod.rest.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by omar.sabry on 1/9/2020.
 */
@Data
@Entity
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_MinutesOfMeeting")
public class MinuteOfMeeting {
    @javax.persistence.Id
    long Id;
    String title;
    String description;
}
