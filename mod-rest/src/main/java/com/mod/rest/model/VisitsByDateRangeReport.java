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
public class VisitsByDateRangeReport {
    @Id
    Long Id;
    @Column(name="nameArabic")
    String sectorName;
    @Column(name="sendOfficalWrite")
    String sendOfficalWrite;
    @Column(name="correlationRatio")
    String correlationRatio;
    @Column(name="date_of_meeting")
    Date dateOfMeeting;
    @Column(name="meetingAttendee")
    String meetingAttendee;
    @Column(name="subSection")
    String subSection;
    @Column(name="UnitName")
    String UnitName;
    @Column(name="correlationImportance")
    String correlationImportance;



    @ColumnName(key = "اسم الجهة")
    public String getSectorName(){
        return Utils.removeNullValue(sectorName);
    }

    @ColumnName(key = "إرسال الكتاب الرسمي")
    public String getSendOfficalWrite(){
        return Utils.removeNullValue(sendOfficalWrite);
    }

    @ColumnName(key = "نسبة الإرتباط")
    public String getCorrelationRatio(){
        return Utils.removeNullValue(correlationRatio);
    }

    @ColumnName(key = "تاريخ الزيارة")
    public Date getDateOfMeeting() {
        return (dateOfMeeting);
    }

    @ColumnName(key = "محضر الاجتماع")
    public String getMeetingAttendee(){
        return meetingAttendee;
    }


    @ColumnName(key = "اللجان الفرعية")
    public String getSubSection(){
        return subSection;
    }

    @ColumnName(key = "الإدارة")
    public String getUnitName(){
        return Utils.removeNullValue(UnitName);
    }

    @ColumnName(key = "أهمية الارتباط")
    public String getCorrelationImportance(){
        return Utils.removeNullValue(correlationImportance);
    }





}