package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */

@Entity

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



    @ColumnName(order = 1, key = "اسم الجهة")
    public String getSectorName(){
        return Utils.removeNullValue(sectorName);
    }

    @ColumnName(order = 2, key = "تصنيف الجهة")
    public String getSectorType(){
        return Utils.removeNullValue(sectorType);
    }

    @ColumnName(order = 3, key = "موقع المذكرة")
    public String getPlaceOfDiary(){
        return Utils.removeNullValue(placeOfDiary);
    }

    @ColumnName(order = 4, key = "تاريخ التوقيع")
    public Date getDateOfDiary(){
        return (dateOfDiary);
    }

    public void setSectorType(String sectorType){
        this.sectorType = sectorType;
    }


    public void setPlaceOfDiary(String placeOfDiary){
        this.placeOfDiary = placeOfDiary;
    }

    @ColumnName(order = 5, key = "تاريخ الانتهاء ")
    public Date getEndDateOfDiary(){
        return endDateOfDiary;
    }


    @ColumnName(order = 6, key = "نوع التعاون في مذكرة التفاهم")
    public String getTypeOfCoopDiary(){
        return typeOfCoopDiary;
    }

    @ColumnName(order = 7, key = "موقف مذكرة التفاهم")
    public String situationDiary(){
        return Utils.removeNullValue(situationDiary);
    }

    @ColumnName(order = 8, key = "آلية التجديد")
    public String getRenewalDiary(){
        return Utils.removeNullValue(renewalDiary);
    }





}