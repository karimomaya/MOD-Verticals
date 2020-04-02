package com.mod.soap.dao.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by karim on 3/9/20.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_meeting")
public class Meeting {

    @javax.persistence.Id
    long Id;
    String Subject;
    String Description;
    Integer MeetingType;
    Date EndDate;
    //    Date endDate;
    Date startDate;
    String meetingGoal;
    Integer status;
    @NotFound(action = NotFoundAction.IGNORE)
    Boolean isPeriodic;
    @NotFound(action = NotFoundAction.IGNORE)
    Integer periodicType;
    @NotFound(action = NotFoundAction.IGNORE)
    Date periodicEndDate;
    Boolean conflict;
    @NotFound(action = NotFoundAction.IGNORE)
    String outlookId;
}

