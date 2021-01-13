package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

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


    @ColumnName(order = 1, key = "اسم الهدف")
    public String getStrategicGoal(){
        String entity = Utils.removeNullValue(strategicGoal);
        return entity;
    }
    @ColumnName(order = 2, key = "تاريخ البدء")
    public String getStartDate(){
//        String entity = Utils.removeNullValue(startDate);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(startDate);
    }
    @ColumnName(order = 3, key = "تاريخ الانتهاء")
    public String getEndDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(endDate);
    }
    @ColumnName(order = 4, key = "نسبة إنجاز الهدف")
    public String getStrategicGoalProgress(){
        String entity = Utils.removeNullValue(strategicGoalProgress);
        return entity;
    }
    @ColumnName(order = 5, key = "اسم المؤشر")
    public String getIndicatorName(){
        String entity = Utils.removeNullValue(indicatorName);
        return entity;
    }
    @ColumnName(order = 6, key = "المسؤول عن المؤشر")
    public String getStrategicIndicatorUnitName(){
        String entity = Utils.removeNullValue(strategicIndicatorUnitName);
        return entity;
    } @ColumnName(order = 7, key = "النصف السنوي")
    public String getHalfOfYear(){
        String entity = Utils.removeNullValue(halfOfYear);
        return entity;
    }
    @ColumnName(order = 8, key = "القيمة المستهدفة")
    public String getTargetValue(){
        String entity = Utils.removeNullValue(targetValue);
        return entity;
    }
    @ColumnName(order = 9, key = "القيمة المحققة")
    public String getAchievedValue(){
        String entity = Utils.removeNullValue(achievedValue);
        return entity;
    }

}
