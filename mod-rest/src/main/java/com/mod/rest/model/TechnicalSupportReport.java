package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Created by omar.sabry on 3/10/2020.
 */

@Entity
@Immutable
public class TechnicalSupportReport {
    @Id
    Long Id;

    String InitiatorUnit;
    Integer total;
    Integer Status;
    Integer evaluation;

    @ColumnName(order = 4, key = "جهة الطلب")
    public String getInitiatorUnit() { return Utils.removeNullValue(InitiatorUnit); }

    @ColumnName(order = 3, key = "عدد الطلبات")
    public Integer getTotal() { return total; }

    @ColumnName(order = 2, key = "تم الحل")
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

    @ColumnName(order = 1, key = "متوسط التقييم")
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
}
