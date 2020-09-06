package com.mod.rest.model;


import com.mod.rest.annotation.PDFResources;
import com.mod.rest.system.Utils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@PDFResources(key="Decision-support-report-template")
public class TaskDecisionSupport {



    @javax.persistence.Id
    long Id;

    String taskName;
    String owner;
    Long createdById;
    Date dueDate;
    Date startDate;
    Integer priority;
    Integer taskStatus; //0:stopped, 1: not started, 2: started, 3: finished 10: draft
    String description;

    String taskProjectName;
    Integer progress;
    String source;

    public String getArabicStartDate(){
        return Utils.convertDateToArabic(this.startDate);
    }
    public String getArabicDueDate(){
        return Utils.convertDateToArabic(this.dueDate);
    }
    public String getProgressPercent(){
        return this.progress + "%";
    }



}
