package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * Created by amira.sherif on 3/16/2020.
 */
@Getter
@Setter
@Entity

public class RiskReportHelper {

    @Id
    long Id;
    String riskNumber;
    String riskName;
    Integer priority;
    Date riskDate;
    Date riskSolutionDate;
    Integer riskState;
    String riskDescription;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    @NotFound(action = NotFoundAction.IGNORE)
    User createdBy;
    boolean isDeleted;
    String notes;
    Date riskCloseDate;
    Integer relatedType;

    @Transient
    ArrayList<RiskResponsibleHelper> responsibles;

    String project;
    String programName;
    String activityName;
    String policyactivity;
    String goal;
    String initiative;
    String kpi;

//    @ManyToOne
//    @JoinColumn(name = "projectName")
//    @NotFound(action = NotFoundAction.IGNORE)
//    Project project;
//    @Transient
//    String projectDescription;

    @ColumnName(order = 1, key = "رقم التحدي")
    public String getRiskNumber(){
        return (String) removeNullValue(this.riskNumber);
    }

    @ColumnName(order = 2, key = "اسم التحدي")
    public String getRiskName(){
        return (String) removeNullValue(this.riskName);
    }

    @ColumnName(order = 3, key = "التاريخ")
    public Date getRiskDate() {
        return this.riskDate;
    }

    @ColumnName(order = 4, key = "تاريخ المتوقع للحل")
    public Date getRiskSolutionExperctedDate() {
        return this.riskSolutionDate;
    }

    @ColumnName(order = 5, key = "وصف التحدي")
    public String getRiskDescription(){
        return (String) removeNullValue(this.riskDescription);
    }

    @ColumnName(order = 6, key = "الأولوية")
    public String getPriority() {
        switch (this.priority){
            case 1:
                return "منخفضة";
            case 5:
                return "متوسطة";
            case 10:
                return "حرجة";
            default:
                return "";
        }
    }

    @ColumnName(order = 7, key = "حالة التحدي")
    public String getState() {
        switch (this.riskState){
            case 1:
                return "مفتوح";
            case 0:
                return "مغلق";
            default:
                return "";
        }
    }

    @ColumnName(order = 8, key = "التحدي مرتبط ب")
    public String getRelatedType() {
        switch (this.relatedType){
            case 1:
                return "مشروع";
            case 2:
                return "نشاط";
            case 3:
                return "هدف";
            case 4:
                return "مبادرة";
            case 5:
                return "برنامج";
            case 6:
                return "أنشطة تنفيذ السياسة";
            case 7:
                return "مؤشر الاداء";
            default:
                return "";
        }
    }

    @ColumnName(order = 9, key="الاسم ")
    public String getProjectName(){
        if (this.relatedType == 1) {
//            Project p = (Project) removeNullValue(this.project);
//            if (p instanceof Project) {
//                return p.getName();
//            }
            return project;
        } else if (this.relatedType == 2) {
//            MainActivity a = (MainActivity) removeNullValue(this.project);
//            if (a instanceof MainActivity) {
//                return a.getActivityName();
//            }
            return activityName;
        } else if (this.relatedType == 3) {
//            IPStrategicGoal a = (IPStrategicGoal) removeNullValue(this.project);
//            if (a instanceof IPStrategicGoal) {
//                return a.getStrategicGoal();
//            }
            return goal;
        }else if (this.relatedType == 4) {
            //            MainActivity a = (MainActivity) removeNullValue(this.project);
//            if (a instanceof MainActivity) {
//                return a.getActivityName();
//            }

            return initiative;
        } else if (this.relatedType == 5) {
//            Program a = (Program) removeNullValue(this.project);
//            if (a instanceof Program) {
//                return a.getName();
//            }
            return programName;
        } else if (this.relatedType == 6) {
//            MainActivity a = (MainActivity) removeNullValue(this.project);
//            if (a instanceof MainActivity) {
//                return a.getActivityName();
//            }
            return policyactivity;
        } else if (this.relatedType == 7) {
//            MainActivity a = (MainActivity) removeNullValue(this.project);
//            if (a instanceof MainActivity) {
//                return a.getActivityName();
//            }
            return kpi;
        } else {

            return "";
        }
    }

//    @ColumnName(order = 1, key="الوصف ")
//    public String getProjectDescription(){
//        if (this.relatedType == 1) {
//            Project p = (Project) removeNullValue(this.project);
//            if (p instanceof Project) {
//                return p.getDescription();
//            }
//            return "";
//        }
//        else {
////            MainActivity a = (MainActivity) removeNullValue(this.project);
////            if (a instanceof MainActivity) {
////                return a.getActivityDescription();
////            }
//            return "";
//        }
//    }
    @ColumnName(order = 10, key = "المنشئ")
    public String getCreatedByName(){
        User user = (User) removeNullValue(this.createdBy);
        if (user instanceof User) return user.getDisplayName();
        return "";
    }
    @ColumnName(order = 11, key = "الملاحظات")
    public String getNotes() {
        return (String) removeNullValue(this.notes);
    }
//    @ColumnName(order = 1, key="المسؤولين عن التحدي")
    public String getResponsibles(){
        String names = "";
        char newline = (char)0x202C;
        for(RiskResponsibleHelper responsible : this.responsibles) {
            names +=  responsible.getResponsible().getDisplayName() + ",";
        }
        return names.substring(0,names.length()-2);
    }
    private Object removeNullValue(Object object){
        if (object == null) return "";
        if (object instanceof String) {
            if (object.equals("null") || object.equals("undefined")){
                return "";
            }
        }
        return object;
    }
}
