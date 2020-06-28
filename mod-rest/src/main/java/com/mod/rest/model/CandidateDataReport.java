package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;

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
@Immutable
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


    @ColumnName(key = "اسم المرشح")
    public String getname(){
        return Utils.removeNullValue(name);
    }

    @ColumnName(key = "المديرية/المكتب")
    public String getDIR(){
        return Utils.removeNullValue(DIR);
    }

    @ColumnName(key = "المسمى الوظيفي")
    public String getproposedVacancy(){
        return Utils.removeNullValue(proposedVacancy);
    }

    @ColumnName(key = "المؤهل العلمي")
    public String getacademicQualification(){
        return Utils.removeNullValue(academicQualification);
    }

    @ColumnName(key = "الخبرات العملية")
    public String getpracticalExperience(){
        if (practicalExperience.equals("undefined")){
            return "";

        }else{
            return Utils.removeNullValue(practicalExperience);
        }

    }

    @ColumnName(key = "الرتبة/الدرجة")
    public String getrank(){
        return Utils.removeNullValue(rank);
    }
    @ColumnName(key = "حالة المرشح")
    public String getstatus(){
        return Utils.removeNullValue(status);
    }

    @ColumnName(key = "ملاحظات")
    public String getnotes(){
        if (notes.equals("undefined")){
            return "";

        }else{
            return Utils.removeNullValue(notes);
        }

    }

}