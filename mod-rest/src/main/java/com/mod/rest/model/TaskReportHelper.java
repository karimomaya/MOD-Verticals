package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by karim.omaya on 1/11/2020.
 */
@Getter
@Setter
@Entity
@Immutable
public class TaskReportHelper {
    @Id
    long Id;

    String taskName;
    @ManyToOne
    @JoinColumn(name = "owner")
    @NotFound(action = NotFoundAction.IGNORE)
    User owner;
    @ManyToOne
    @JoinColumn(name = "createdBy")
    @NotFound(action = NotFoundAction.IGNORE)
    User createdBy;
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

    @ColumnName(key = "اسم")
    public String getTaskName(){
        return (String) removeNullValue(this.taskName);
    }

    @ColumnName(key = "منشأ")
    public String getCreatedByName(){
        User user = (User) removeNullValue(this.createdBy);
        if (user instanceof User) return user.getDisplayName();
        return "";
    }

    @ColumnName(key = "مالك")
    public String getOwnerName(){
        User user = (User) removeNullValue(this.owner);
        if (user instanceof User) return user.getDisplayName();
        return "";
    }

    @ColumnName(key = "وصف")
    public String getDescription(){
        return (String) removeNullValue(this.description);
    }

    @ColumnName(key = "حالة")
    public String getTaskStatus() { //0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate, 12: archived
        switch (this.status){
            case 0:
                return "متوقفة";
            case 1:
                return "لم تبدأ";
            case 2:
                return "بدء";
            case 3:
                return "منتهية";
            case 11:
                return "اغلقت بقوه";
            case 12:
                return "مؤرشفة";
            default:
                return "";
        }
    }
    @ColumnName(key = "تاريخ البدء")
    public Date getTaskStartDate(){
       return this.startDate;
    }

    @ColumnName(key = "تاريخ الانتهاء")
    public Date getTaskDueDate(){
        return this.dueDate;
    }

    @ColumnName(key = "نسبة الأنجاز")
    public String getProgressPercentage(){
        return this.progress+"%";
    }

    @ColumnName(key="المشروع")
    public String getProjectName(){
        Project p = (Project) removeNullValue(this.project);
        if (p instanceof Project) {
            return p.getName();
        }
        return "";
    }

    private Object removeNullValue(Object object){
        if (object == null) return "";
        if (object instanceof String) {
            if (object.equals("null")){
                return "";
            }
        }

        return object;
    }
}
