package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by abdallah.shaaban on 7/26/2020.
 */
@Entity
@Immutable
@Data
public class IPOperationalIndicatorReport {
    @Id
    String operationalIndicatorName;
    String operationalIndicatorUnitName_ar;
    String operationalIndicatorUnitName_en;
    String targetValue;
    String achievedValue;
    String quarter;
    @ColumnName(key = "اسم المؤشر التشغيلي")
    public String getOperationalIndicatorName(){
        String entity = Utils.removeNullValue(operationalIndicatorName);
        return entity;
    }
    @ColumnName(key = "الجهة المسؤولة")
    public String unitNameStr(){
        String entity = Utils.removeNullValue(operationalIndicatorUnitName_ar);
        return entity;
    }
    @ColumnName(key = "حالة المؤشر")
    public String getStartDateStr(){


        if(achievedValue !=null && targetValue != null){
            int tmp = (int)(Double.valueOf(achievedValue) / Double.valueOf(targetValue)*100);
            if(tmp == 100){
                return "100%";
            }else if (tmp <100 && tmp >= 75){
                return "75% حتي 100%";
            }else{
                return "أقل من 75%";
            }
        }
        return "لم يتم قياس المؤشر";
    }
    @ColumnName(key = "المستهدف")
    public String getEndDateStr(){
        String entity = Utils.removeNullValue(targetValue);
        return entity;
    }
    @ColumnName(key = "المحقق")
    public String getClassification(){
        String entity = Utils.removeNullValue(achievedValue);
        return entity;
    }
    @ColumnName(key = "نسبة الإنجاز")
    public String getProgressStr(){
        if(achievedValue !=null && targetValue != null){
        int tmp = (int)(Double.valueOf(achievedValue) / Double.valueOf(targetValue)*100);
        String tmpStr = String.valueOf(tmp);
            return tmpStr+" %";
        }
        return "";
    }
}
