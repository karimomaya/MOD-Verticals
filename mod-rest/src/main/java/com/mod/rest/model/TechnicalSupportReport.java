package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Created by omar.sabry on 3/10/2020.
 */

@Entity
@Immutable
public class TechnicalSupportReport {
    @Id
    Integer Id;

    String InitiatorUnit;
    Integer total;
    Integer Status;
    Integer evaluation;



    @ColumnName(key = "جهة الطلب")
    public String getInitiatorUnit() { return removeNullValue(InitiatorUnit); }

    @ColumnName(key = "عدد الطلبات")
    public Integer getTotal() { return total; }

    @ColumnName(key = "تم الحل")
    public String getStatus() {
        switch (Status){
            case 0:
                return "تحت الإجراء";
            case 1:
                return "تم الحل";
            default:
                return "إعادة الطلب";
        }
    }

    @ColumnName(key = "متوسط التقييم")
    public String getEvaluation() {
        if(evaluation == null){
            evaluation = 0;
        }
        switch (evaluation){
            case 1:
                return "مقبول";
            case 2:
                return "جيد";
            case 3:
                return "جيد جداً";
            case 4:
                return "ممتاز";
            default:
                return "-";
        }
    }

    private String removeNullValue(String value){
        if (value == null) return "";
        if (value.equals("null")){
            return "";
        }
        return value;
    }
}