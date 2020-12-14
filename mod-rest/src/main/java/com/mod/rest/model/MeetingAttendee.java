package com.mod.rest.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@Data
@Entity
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_Attendee")
public class MeetingAttendee {
    @Id
    long Id;
    String displayName;
    Integer status;
    String roleName;
    Boolean attendedMeeting;
    Boolean isExternal;
    // Internal Attendee
    @Transient
    String userDisplayName;
    @Transient
    String RoleName;
    @Transient
    String RoleName_ar;
    @Transient
    String title;
    // External Attendee
    @Transient
    String nameArabic;
    @Transient
    String positionArabic;
    @Transient
    String entityName;

    String meeting_to_attendees_Id;

    public String getAttendeeName(){
        if(this.isExternal){
            return this.nameArabic;
        }else{
            return this.userDisplayName;
        }
    }

    public String getAttendeeRole(){
        if(this.isExternal){
            return this.positionArabic;
        }else{
            return this.RoleName_ar;
        }
    }

    public String getStatusType(){
        switch (this.status){
            case 1:
                return "حاضر";

            case 2:
                return "لم يحضر";
        }
        return "لم يقرر بعد";
    }
}
