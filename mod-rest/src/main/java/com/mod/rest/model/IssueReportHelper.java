package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amira.sherif on 3/16/2020.
 */
@Getter
@Setter
@Entity
@Immutable
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

    @ManyToOne
    @JoinColumn(name = "projectName")
    @NotFound(action = NotFoundAction.IGNORE)
    Project project;

    @Transient
    String projectDescription;

    @ColumnName(key = "رقم الخطر")
    public String getIssueId(){
        return (String) removeNullValue(this.issueId);
    }

    @ColumnName(key = "اسم الخطر")
    public String getIssueName(){
        return (String) removeNullValue(this.issueName);
    }

    @ColumnName(key = "وصف الخطر")
    public String getIssueDescription(){
        return (String) removeNullValue(this.issueDescription);
    }

    @ColumnName(key = "الأولوية")
    public String getPriority() {
        switch (this.priority){
            case 0:
                return "منخفضة";
            case 5:
                return "متوسطة";
            case 10:
                return "حرجة";
            default:
                return "";
        }
    }

    @ColumnName(key = "حالة الخطر")
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

    @ColumnName(key = "الاحتمالية")
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

    @ColumnName(key = "التأثير")
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
    @ColumnName(key = "نطاق التأثير")
    public String getEffectArea() {
        return (String) removeNullValue(this.effectArea);
    }

    @ColumnName(key = "القرار")
    public String getDecision() {
        return (String) removeNullValue(this.decision);
    }

    @ColumnName(key = "الملاحظات")
    public String getNotes() {
        return (String) removeNullValue(this.notes);
    }

    @ColumnName(key = "الخطر مرتبط ب")
    public String getRelatedType() {
        switch (this.relatedType){
            case 1:
                return "مشروع";
            case 2:
                return "نشاط";
            default:
                return "";
        }
    }

    @ColumnName(key="اسم المشروع/النشاط")
    public String getProjectName(){
        if (this.relatedType == 1) {
            Project p = (Project) removeNullValue(this.project);
            if (p != null) {
                return p.getName();
            }
            return "";
        }
        else {
            MainActivity a = (MainActivity) removeNullValue(this.project);
            if (a != null) {
                return a.getActivityName();
            }
            return "";
        }
    }

    @ColumnName(key="وصف المشروع/النشاط")
    public String getProjectDescription(){
        if (this.relatedType == 1) {
            Project p = (Project) removeNullValue(this.project);
            if (p != null) {
                return p.getDescription();
            }
            return "";
        }
        else {
            MainActivity a = (MainActivity) removeNullValue(this.project);
            if (a != null) {
                return a.getActivityDescription();
            }
            return "";
        }
    }

    @ColumnName(key = "التاريخ")
    public Date getIssueStartDate() {
        return this.issueStartDate;
    }

    @ColumnName(key = "تاريخ المتوقع للحل")
    public Date getIssueEndDate() {
        return this.issueEndDate;
    }

    @ColumnName(key = "المنشئ")
    public String getCreatedByName(){
        User user = (User) removeNullValue(this.createdBy);
        if (user != null) return user.getDisplayName();
        return "";
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
