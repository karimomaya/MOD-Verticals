package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by aly.aboulgheit on 5/28/2020.
 */

@Entity
public class CandidateDataReportByStatus {
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
    @Column(name="name")
    String name;
    @Column(name="DIR")
    String DIR;
    @Column(name="totalMark")
    String totalMark;
    @Column(name="yearsOfExp")
    String yearsOfExp;
    @Column(name="lastSalary")
    String lastSalary;

    @ColumnName(order = 1, key = "اسم المرشح")
    public String getname(){
        return Utils.removeNullValue(name);
    }

    @ColumnName(order = 1, key = "سنوات الخبرة")
    public String getyearsOfExp(){
        return Utils.removeNullValue(yearsOfExp);
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

    @ColumnName(order = 1, key = "درجة المرشح")
    public String gettotalMark(){
        if(totalMark == null){
            return "";
        }else if (totalMark.equals("undefined")){
            return "";

        }else{
            return Utils.removeNullValue(totalMark);
        }
    }
    @ColumnName(order = 1, key = "آخر راتب تم استلامه")
    public String getlastSalary(){
        return Utils.removeNullValue(lastSalary);
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