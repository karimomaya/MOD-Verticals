package com.mod.rest.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    String userDisplayName;
    String RoleName;
    String title;
    // External Attendee
    String nameArabic;
    String positionArabic;
    String entityName;

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
            return this.RoleName;
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
