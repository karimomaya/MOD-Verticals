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
public class TranslationByMonthReport {
    @Id
     Long Id;
    @Column(name="UnitName")
    String unitName;
    @Column(name="translation_words_count")
    String translationWordsCount;
    @Column(name="translation_pages_count")
    String translationPagesCount;
    @Column(name="Number_Of_Requests")
    String numberOfRequests;


    @ColumnName(order = 1, key = "جهة الطلب")
    public String getUnitName(){
        return Utils.removeNullValue(unitName);
    }

    @ColumnName(order = 2, key = "عدد الصفحات")
    public String getTranslationWordsCount(){
        return Utils.removeNullValue(translationWordsCount);
    }

    @ColumnName(order = 3, key = "عدد الكلامات")
    public String getTranslationPagesCount(){
        return Utils.removeNullValue(translationPagesCount);
    }

    @ColumnName(order = 4, key = "عدد الطلبات")
    public String getNumberOfRequests(){
        return Utils.removeNullValue(numberOfRequests);
    }



}