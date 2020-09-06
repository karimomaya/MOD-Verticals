package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.annotation.PDFResources;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by karim.omaya on 10/29/2019.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyTaskManagementMOD_TM_entity_Task")
@PDFResources(key="task-report-template")
public class Task implements Serializable {

//    select task.Id, task.taskName, person.DisplayName as owner, task.source,
//    task.status as taskStatus, task.dueDate, task.startDate, task.priority,
//    ISNULL(([dbo].[GetTotalFinishedWorkingUsers](task.Id)/ NULLIF([dbo].[GetTotalWorkingUsers](task.Id),0)),0) as finished,
//    task.description, taskProject.name as taskProjectName,  task.owner as ownerId, task.createdBy as createdById,
//    task.progress from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task

    @Id
    long Id;

    String taskName;
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
    Integer progress;
    String entityBWSId;
    String source;
    Integer integrationId;
    Integer typeOfAssignment;
    /*@OneToMany( mappedBy="task", fetch = FetchType.EAGER)
    List<TaskPerformer> taskPerformers;*/
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
