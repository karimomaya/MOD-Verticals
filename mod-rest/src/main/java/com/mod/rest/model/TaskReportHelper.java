package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by karim.omaya on 1/11/2020.
 */
@Getter
@Setter
@Entity

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
    @Transient
    String number;
//    @NotFound(action = NotFoundAction.IGNORE)
    @Transient
    ArrayList<TaskPerformer> performers;

    public void setNumber(int position){
        String result = "البند ";
        this.number =  result + convertPositionToWord(position);
    }

    public String getNumber(){
        return this.number;
    }

    public String convertPositionToWord(int position){
        switch (position){
            case 1:
                return "الأول";
            case 2:
                return "الثاني";
            case 3:
                return "الثالث";
            case 4:
                return "الرابع";
            case 5:
                return "الخامس";
            case 6:
                return "السادس";
            case 7:
                return "السابع";
            case 8:
                return "الثامن";
            case 9:
                return "التاسع";
            case 10:
                return "العاشر";
            case 11:
                return "الحادي عشر";
            case 12:
                return "الثاني عشر";
            default:
                return position+"";
        }
    }

    @ColumnName(order = 1, key = "اسم")
    public String getTaskName(){
        return (String) removeNullValue(this.taskName);
    }

    @ColumnName(order = 2, key = "منشأ")
    public String getCreatedByName(){
        User user = (User) removeNullValue(this.createdBy);
        if (user instanceof User) return user.getDisplayName();
        return "";
    }

    @ColumnName(order = 3, key = "مالك")
    public String getOwnerName(){
        User user = (User) removeNullValue(this.owner);
        if (user instanceof User) return user.getDisplayName();
        return "";
    }

    @ColumnName(order = 4, key = "وصف")
    public String getDescription(){
        return (String) removeNullValue(this.description);
    }

    @ColumnName(order = 5, key = "حالة")
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
    @ColumnName(order = 6, key = "تاريخ البدء")
    public Date getTaskStartDate(){
       return this.startDate;
    }

    @ColumnName(order = 7, key = "تاريخ الانتهاء")
    public Date getTaskDueDate(){
        return this.dueDate;
    }

    public String getArabicTaskStartDate() {
        return Utils.convertDateToArabic(this.startDate);
    }

    public String getArabicTaskDueDate(){
        return Utils.convertDateToArabic(this.dueDate);
    }

    @ColumnName(order = 8, key = "نسبة الأنجاز")
    public String getProgressPercentage(){
        return this.progress+"%";
    }

    @ColumnName(order = 9, key="المشروع")
    public String getProjectName(){
        if(this.project == null) return "";
        Project p = (Project) removeNullValue(this.project);
        if (p instanceof Project) {
            return p.getName();
        }
        return "";
    }

    public String getPerformersNames() {
        String names = "";
        char newline = (char)0x202C;
        for(TaskPerformer performer : this.performers) {
            names +=  performer.getDisplayName() + ", ";
        }
        return names.substring(0,names.length()-2);
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
