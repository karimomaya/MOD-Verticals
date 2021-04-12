package com.mod.rest.model;

import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by omaradl on 7/15/2020.
 */
@Getter
@Setter
@Entity

public class TaskWithPerformer {
    @Id
    @Column(name="RowNum")
    long RowNum;

    long Id;

    String taskName;
    Integer progress;
    Long owner;
    Long createdBy;
    Date dueDate;
    Date startDate;
    Integer priority;
    Integer status; //0:stopped, 1: not started, 2: started, 3: finished 10: draft
    String description;
    @ManyToOne
    @JoinColumn(name = "taskProjectId")
    @NotFound(action = NotFoundAction.IGNORE)
    Project project;
    Boolean singleOrMultiple;
    String entityBWSId;
    String source;
    Integer integrationId;
    Integer typeOfAssignment;

    Long performerId;
    String performerName;
    Integer performerStatus;
    Integer performerProgress;

    @Transient
    User userCreatedBy;
    @Transient
    User userOwner;
    @Transient
    User performer;
    @Transient
    String taskPerformerStatus;

    public String getArabicStartDate(){
        return Utils.convertDateToArabic(this.startDate);
    }
    public String getArabicDueDate(){
        return Utils.convertDateToArabic(this.dueDate);
    }
    public String getProgressPercent(){
        return this.progress + "%";
    }

    public String getOwnerame(){
        return this.userOwner.getDisplayName();
    }

    public String getProjectName(){
        if(this.project== null){
            return "";
        }else{
            return this.project.getName();
        }
    }

    public String getTaskPerformerStatus() {
        Date tomorrowDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tomorrowDate);
        c.add(Calendar.DATE, 1);
        tomorrowDate = c.getTime();

        if(this.getStatus() == 3 || this.getStatus() == 12 ||(this.getStatus() == 2 && this.getPerformerStatus() == 2)){
            return "finished";
        }else if(this.getDueDate().before(tomorrowDate) && (this.getStatus() == 1 || (this.getStatus() == 2 && this.getPerformerStatus() <= 1))){
            return "delayed";
        }else{
            return "inProgress";
        }
    }
}
