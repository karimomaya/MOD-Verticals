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
public class DiaryOfUnderstandingReport {
    @Id
    Long Id;
    @Column(name="nameArabic")
    String sectorName;
    @Column(name="type")
    String sectorType;
    @Column(name="placeOfDiary")
    String placeOfDiary;
    @Column(name="dateOfDiary")
    Date dateOfDiary;
    @Column(name="endDateOfDiary")
    Date endDateOfDiary;
    @Column(name="typeOfCoopDiary")
    String typeOfCoopDiary;
    @Column(name="situationDiary")
    String situationDiary;
    @Column(name="renewalDiary")
    String renewalDiary;



    @ColumnName(key = "اسم الجهة")
    public String getSectorName(){
        return Utils.removeNullValue(sectorName);
    }

    @ColumnName(key = "تصنيف الجهة")
    public String getSectorType(){
        return Utils.removeNullValue(sectorType);
    }

    @ColumnName(key = "موقع المذكرة")
    public String getPlaceOfDiary(){
        return Utils.removeNullValue(placeOfDiary);
    }

    @ColumnName(key = "تاريخ التوقيع")
    public Date getDateOfDiary(){
        return (dateOfDiary);
    }

    public void setSectorType(String sectorType){
        this.sectorType = sectorType;
    }


    public void setPlaceOfDiary(String placeOfDiary){
        this.placeOfDiary = placeOfDiary;
    }

    @ColumnName(key = "تاريخ الانتهاء ")
    public Date getEndDateOfDiary(){
        return endDateOfDiary;
    }


    @ColumnName(key = "نوع التعاون في مذكرة التفاهم")
    public String getTypeOfCoopDiary(){
        return typeOfCoopDiary;
    }

    @ColumnName(key = "موقف مذكرة التفاهم")
    public String situationDiary(){
        return Utils.removeNullValue(situationDiary);
    }

    @ColumnName(key = "آلية التجديد")
    public String getRenewalDiary(){
        return Utils.removeNullValue(renewalDiary);
    }





}