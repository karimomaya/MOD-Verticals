package com.mod.rest.model;

import com.mod.rest.annotation.PDFResources;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_meeting")
@PDFResources(key="meeting-template")
public class Meeting {
    @Id
    long Id;
    String Subject;
    String Description;
    Integer MeetingType;
    Date EndDate;
    Date startDate;
    String meetingGoal;
    Integer status;
    @ManyToOne
    @JoinColumn(name = "Owner")
    @NotFound(action = NotFoundAction.IGNORE)
    User owner;
    @ManyToOne
    @JoinColumn(name = "Room_to_Meeting_Id")
    @NotFound(action = NotFoundAction.IGNORE)
    Room room;
}
