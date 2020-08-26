package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */

@Entity
@Immutable
public class PoliticalAdviceReport {
    @Id
    Long Id;
    @Column(name="Approver")
    String approver;
    @Column(name="title")
    String title;
    @Column(name="type")
    String type;
    @Column(name="DIR")
    String DIR;
    @Column(name="Employee")
    String employee;
    @Column(name="ApprovedDate")
    LocalDate approvedDate;
    @Column(name="Evaluation")
    String evaluation;
    @Column(name="CreatedDate")
    LocalDate createdDate;
    @Column(name="PoliticalType")
    String politicalType;
    @Column(name="Comment")
    String comment;
    @Column(name="Code")
    String code;
    @Column(name="SEC")
    String SEC;




    @ColumnName(key = "موضوع المشورة")
    public String getTitle(){
        return Utils.removeNullValue(title);
    }

    @ColumnName(key = "جهة إعداد المشورة")
    public String getDIR(){
        return Utils.removeNullValue(DIR);
    }

    @ColumnName(key = "القسم المسؤول")
    public String getSEC(){
        return Utils.removeNullValue(SEC);
    }

    @ColumnName(key = "تقييم المشورة")
    public String getEvaluation(){
        if (evaluation != null){
            //setTranslationAverageEvaluation("لم يتم التقييم");

            return "لم يتم التقييم";
        }else{
            return Utils.removeNullValue(evaluation);
        }

    }

    @ColumnName(key = "معد المشورة")
    public String getEmployee(){
        return Utils.removeNullValue(employee);
    }

    @ColumnName(key = "تصنيف المشورة")
    public String getType(){
        return Utils.removeNullValue(type);
    }



    @ColumnName(key = "جهة اعتماد المشورة")
    public String getApprover(){
        return Utils.removeNullValue(approver);
    }

    @ColumnName(key = "نوع المشورة")
    public String getPoliticalType(){
        return Utils.removeNullValue(politicalType);
    }

    @ColumnName(key = "ملاحظات")
    public String getComment(){
        return Utils.removeNullValue(comment);
    }

    @ColumnName(key = "رمز المشورة")
    public String getCode(){
        return Utils.removeNullValue(code);
    }

    @ColumnName(key = "تاريخ اعتماد المشورة")
    public LocalDate getApprovedDate(){
        return approvedDate;
    }

    @ColumnName(key = "تاريخ إعداد المشورة")
    public LocalDate getCreatedDate(){
        return createdDate;
    }

}