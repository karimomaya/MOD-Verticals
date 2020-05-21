package com.mod.rest.model;

import com.mod.rest.annotation.PDFResources;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

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
    String description;
    Integer MeetingType;
    Date EndDate;
//    Date endDate;
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


    public String getArabicDate(){
        return Utils.convertDateToArabic(this.startDate);
    }

    public String getMeetingRoomName(){
        return this.room.getRoomName();
    }

    public String getHourOfMeeting(){
        return Utils.getHoursFromDate(this.startDate);
    }

    public String getMeetingSubject() {
        return "محضر اجتماع "+ this.Subject;
    }

    public String getDayOfWeek(){
        return Utils.getArabicNameOfDay(this.startDate);
    }

    public String getOwnerName(){
        return owner.getDisplayName();
    }

    public String toString(){ return "ID="+this.getId()+" Subject="+this.getSubject()+" Description="+this.getDescription()
            +" status="+this.getStatus();
    }

}
