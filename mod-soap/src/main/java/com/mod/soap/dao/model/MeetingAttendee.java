package com.mod.soap.dao.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by karim on 3/9/20.
 */
@Data
@Entity
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_Attendee")
public class MeetingAttendee {
    @javax.persistence.Id
    long Id;
    String displayName;
    Integer status;
    String roleName;
    Boolean attendedMeeting;
    Boolean isExternal;
    // Internal Attendee
    String userDisplayName;
    String RoleName;
    String title;
    // External Attendee
    String nameArabic;
    String positionArabic;
    String entityName;
    long attendeeID;

}
