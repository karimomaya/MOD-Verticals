package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.persistence.Entity;
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

public class IssueReportHelper {

    @Id
    long Id;

    String issueId;
    String issueName;
    Integer probability;
    Integer effect;
    String effectArea;
    Date issueStartDate;
    Date issueEndDate;
    Integer issueStatus;
    String issueDescription;
    Date IssueCloseDate;
    String decision;
    Integer priority;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    @NotFound(action = NotFoundAction.IGNORE)
    User createdBy;
    boolean isDeleted;
    String notes;
    Integer relatedType;

//    @Transient
//    ArrayList<IssueResponsibleHelper> responsibles;
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
//
//    @Transient
//    String projectDescription;

    @ColumnName(order = 1, key = "رقم الخطر")
    public String getIssueId(){
        return (String) removeNullValue(this.issueId);
    }

    @ColumnName(order = 2, key = "اسم الخطر")
    public String getIssueName(){
        return (String) removeNullValue(this.issueName);
    }

    @ColumnName(order = 3, key = "وصف الخطر")
    public String getIssueDescription(){
        return (String) removeNullValue(this.issueDescription);
    }
    @ColumnName(order = 4, key = "التاريخ")
    public Date getIssueStartDate() {
        return this.issueStartDate;
    }

    @ColumnName(order = 5, key = "تاريخ المتوقع للحل")
    public Date getIssueEndDate() {
        return this.issueEndDate;
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

    @ColumnName(order = 7, key = "حالة الخطر")
    public String getState() {
        switch (this.issueStatus){
            case 1:
                return "مفتوح";
            case 0:
                return "مغلق";
            default:
                return "";
        }
    }

    @ColumnName(order = 8, key = "الاحتمالية")
    public String getProbability() {
        switch (this.probability){
            case 1:
                return "منخفضة";
            case 2:
                return "متوسطة";
            case 3:
                return "عالية";
            default:
                return "";
        }
    }

    @ColumnName(order = 9, key = "التأثير")
    public String getEffect() {
        switch (this.effect){
            case 1:
                return "منخفض";
            case 2:
                return "متوسط";
            case 3:
                return "عالي";
            default:
                return "";
        }
    }
    @ColumnName(order = 10, key = "نطاق التأثير")
    public String getEffectArea() {
        return (String) removeNullValue(this.effectArea);
    }

    @ColumnName(order = 11, key = "القرار")
    public String getDecision() {
        return (String) removeNullValue(this.decision);
    }

    @ColumnName(order = 12, key = "الخطر مرتبط ب")
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

    @ColumnName(order = 13, key="الاسم ")
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

//    @ColumnName(order = 1, key="وصف المشروع/النشاط")
//    public String getProjectDescription(){
//        if (this.relatedType == 1) {
//            Project p = (Project) removeNullValue(this.project);
//            if (p != null) {
//                return p.getDescription();
//            }
//            return "";
//        }
//        else {
//            MainActivity a = (MainActivity) removeNullValue(this.project);
//            if (a != null) {
//                return a.getActivityDescription();
//            }
//            return "";
//        }
//    }



    @ColumnName(order = 14, key = "المنشئ")
    public String getCreatedByName(){
        User user = (User) removeNullValue(this.createdBy);
        if (user != null) return user.getDisplayName();
        return "";
    }
    @ColumnName(order = 15, key = "الملاحظات")
    public String getNotes() {
        return (String) removeNullValue(this.notes);
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
