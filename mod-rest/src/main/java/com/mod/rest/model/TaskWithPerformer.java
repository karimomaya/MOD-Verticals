package com.mod.rest.model;

import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by omaradl on 7/15/2020.
 */
@Getter
@Setter
@Entity
@Immutable
public class TaskWithPerformer {
    @Id
    @Column(name="Id")
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

    Integer performerId;
    String performerName;
    Integer performerStatus;
    Integer performerProgress;

    @Transient
    User userCreatedBy;
    @Transient
    User userOwner;

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
}
