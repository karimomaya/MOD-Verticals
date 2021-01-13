package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 5/28/2020.
 */

@Entity
public class CandidateDataReport {
    @Id
    Long candidateId;
    @Column(name="proposedVacancy")
    String proposedVacancy;
    @Column(name="academicQualification")
    String academicQualification;
    @Column(name="practicalExperience")
    String practicalExperience;
    @Column(name="notes")
    String notes;
    @Column(name="rank")
    String rank;
    @Column(name="status")
    String status;
    @Column(name="name")
    String name;
    @Column(name="DIR")
    String DIR;


    @ColumnName(order = 1, key = "اسم المرشح")
    public String getname(){
        return Utils.removeNullValue(name);
    }

    @ColumnName(order = 1, key = "المديرية/المكتب")
    public String getDIR(){
        return Utils.removeNullValue(DIR);
    }

    @ColumnName(order = 1, key = "المسمى الوظيفي")
    public String getproposedVacancy(){
        return Utils.removeNullValue(proposedVacancy);
    }

    @ColumnName(order = 1, key = "المؤهل العلمي")
    public String getacademicQualification(){
        return Utils.removeNullValue(academicQualification);
    }

    @ColumnName(order = 1, key = "الخبرات العملية")
    public String getpracticalExperience(){
        if(practicalExperience == null){
            return "";
        }else if (practicalExperience.equals("undefined")){
            return "";
        }else{
            return Utils.removeNullValue(practicalExperience);
        }

    }

    @ColumnName(order = 1, key = "الرتبة/الدرجة")
    public String getrank(){
        return Utils.removeNullValue(rank);
    }
    @ColumnName(order = 1, key = "حالة المرشح")
    public String getstatus(){
        return Utils.removeNullValue(status);
    }

    @ColumnName(order = 1, key = "ملاحظات")
    public String getnotes(){
        if(notes == null){
            return "";
        }else if (notes.equals("undefined")){
            return "";
        }else{
            return Utils.removeNullValue(notes);
        }

    }

}