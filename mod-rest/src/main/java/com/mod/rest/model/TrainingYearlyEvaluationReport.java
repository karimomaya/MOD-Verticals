package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */

@Entity
@Immutable
public class TrainingYearlyEvaluationReport {
    @Id
    Long Id;
    @Column(name="name_of_the_course")
    String nameOfCourse;
    @Column(name="countEvalu")
    String countEvalu;
    @Column(name="average_time")
    String averageTime;
    @Column(name="average_quality")
    String averageQuality;
    @Column(name="average_skill")
    String averageSkill;
    @Column(name="date_from")
    Date dateFrom;
    @Column(name="date_to")
    Date dateTo;
    @Column(name="average_usereval")
    String averageUserEval;
    @Column(name="average_userevalPercentage")
    String averageUserEvalPercentage;
    @Column(name="summary_lecturersOverallScore")
    String summaryLecturersOverallScore;
    @Column(name="summary_coursesOverallScore")
    String summaryCoursesOverallScore;
    @Column(name="duration")
    String duration;
    @Column(name="summary_numberOfAttendances")
    String summaryNumberOfAttendances;
    @Column(name="lecturer_name")
    String lecturerName;
    @Column(name="End_date")
    String endDate;



    @ColumnName(key = "اسم الدورة")
    public String getNameOfCourse(){
        return Utils.removeNullValue(nameOfCourse);
    }

//    @ColumnName(key = "النسبة المئوية للمحاضر")
//    public String getLecturerPercentage(){
//        if (!summaryLecturersOverallScore.equals(null)){
//            float x = (Float.parseFloat(summaryLecturersOverallScore) / 5) * 100;
//            return Float.toString(x);
//        }else{
//            return "";
//        }
//
//    }
//
//    @ColumnName(key = "النسبة المئوية للبرنامج التدريبي")
//    public String getCoursePercentage(){
//        if (!summaryCoursesOverallScore.equals(null)){
//            float x = (Float.parseFloat(summaryCoursesOverallScore) / 5) * 100;
//            return Float.toString(x);
//        }else{
//            return "";
//        }
//    }
//
//    @ColumnName(key = "مجموع معدل النسبة المئوية لتقييم المحاضر/البرنامج التدريبي")
//    public String getCourseOnLectuer(){
//        if (!summaryCoursesOverallScore.equals(null) && !summaryLecturersOverallScore.equals(null)){
//            float x = (((Float.parseFloat(summaryCoursesOverallScore) / 5) * 100) + ((Float.parseFloat(summaryLecturersOverallScore) / 5) * 100)) / 2;
//            return Float.toString(x);
//        }else{
//            return "";
//        }
//    }


    @ColumnName(key = "تاريخ انعقاد الدروة")
    public Date getDateFrom(){
        return dateFrom;
    }


    @ColumnName(key = "تاريخ إنتهاء الدورة")
    public Date getRequestDate(){
        return dateTo;
    }

    @ColumnName(key = "عدد المنسبين")
    public String getSummaryNumberOfAttendances(){

            return Utils.removeNullValue(summaryNumberOfAttendances);


    }


    @ColumnName(key = "مقدم الخدمة")
    public String getLecturerName(){
        return Utils.removeNullValue(lecturerName);
    }



    @ColumnName(key = "تقييم المحاضر")
    public String getSummaryLecturersOverallScore(){
        return summaryLecturersOverallScore;
    }

    public void setSummaryLecturersOverallScore(String summaryLecturersOverallScore){
        this.summaryLecturersOverallScore = summaryLecturersOverallScore;
    }

    @ColumnName(key = "تقييم البرنامج التدريبي")
    public String getSummaryCoursesOverallScore(){
        return Utils.removeNullValue(summaryCoursesOverallScore);
    }

    public void setSummaryCoursesOverallScore(String summaryCoursesOverallScore){
        this.summaryCoursesOverallScore = summaryCoursesOverallScore;
    }

    @ColumnName(key = "مدة العقاد الدورة بالأيام")
    public String getDuration(){
        return Utils.removeNullValue(duration);
    }

    @ColumnName(key = "تاريخ استحقاق العائد من الإستثمار")
    public String getEndDate(){
        return endDate;
    }

    @ColumnName(key = "نسبة تطوير الموظفين من الدورة")
    public String getAverageUserEvalPercentage(){
        return Utils.removeNullValue(averageUserEvalPercentage);
    }

    @ColumnName(key = "قياس نقاط الاستفادة")
    public String getAverageUserEval(){
        return Utils.removeNullValue(averageUserEval);
    }

    public void setAverageUserEval(String averageUserEval){
        this.averageUserEval = averageUserEval;
    }

    @ColumnName(key = "نسبة تأثير التدريب على مدى استغلال الوقت في العمل")
    public String getAverageTimePercentage(){
        return Utils.removeNullValue(averageTime);
    }

    @ColumnName(key = "نسبة تأثر التدريب على التحسن في الإنتاجية وجودة العمل")
    public String getAverageQuality(){
        return Utils.removeNullValue(averageQuality);
    }

    @ColumnName(key = "نسبة تأثر التدريب في العمل على تطوير المهارات المكتسبة ")
    public String getAverageSkill(){
        return Utils.removeNullValue(averageSkill);
    }


}