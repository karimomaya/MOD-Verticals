package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abdallah.shaaban on 7/6/2020.
 */

@Entity
@Immutable
@Data
public class IPStrategicGoal {
    @Id
    Long orderID;
    String strategicGoal;
    Date startDate;
    Date endDate;
    String strategicGoalUnitName;
    String strategicGoalProgress;
    Long strategicIndicatorId;
    String indicatorName;
    String strategicIndicatorUnitName;
    String halfOfYear;
    String targetValue;
    String achievedValue;


    @ColumnName(key = "اسم الهدف")
    public String getStrategicGoal(){
        String entity = Utils.removeNullValue(strategicGoal);
        return entity;
    }
    @ColumnName(key = "تاريخ البدء")
    public String getStartDate(){
//        String entity = Utils.removeNullValue(startDate);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(startDate);
    }
    @ColumnName(key = "تاريخ الانتهاء")
    public String getEndDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(endDate);
    }
    @ColumnName(key = "نسبة إنجاز الهدف")
    public String getStrategicGoalProgress(){
        String entity = Utils.removeNullValue(strategicGoalProgress);
        return entity;
    }
    @ColumnName(key = "اسم المؤشر")
    public String getIndicatorName(){
        String entity = Utils.removeNullValue(indicatorName);
        return entity;
    }
    @ColumnName(key = "المسؤول عن المؤشر")
    public String getStrategicIndicatorUnitName(){
        String entity = Utils.removeNullValue(strategicIndicatorUnitName);
        return entity;
    } @ColumnName(key = "النصف السنوي")
    public String getHalfOfYear(){
        String entity = Utils.removeNullValue(halfOfYear);
        return entity;
    }
    @ColumnName(key = "القيمة المستهدفة")
    public String getTargetValue(){
        String entity = Utils.removeNullValue(targetValue);
        return entity;
    }
    @ColumnName(key = "القيمة المحققة")
    public String getAchievedValue(){
        String entity = Utils.removeNullValue(achievedValue);
        return entity;
    }

}
