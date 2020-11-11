package com.mod.rest.model;


import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abdallah.shaaban on 7/19/2020.
 */

@Entity
@Immutable
@Data
public class IPSubActivity {
    @Id
    long Id;
    Date startDate;
    Date endDate;
    String subActivityName;
    String unitName_ar;
    String unitName_en;
    Float progress;
    String classification;

    @ColumnName(key = "اسم النشاط الفرعي")
    public String getSubActivityName(){
        String entity = Utils.removeNullValue(subActivityName);
        return entity;
    }
    @ColumnName(key = "الجهة المسؤولة")
    public String unitNameStr(){
        String entity = Utils.removeNullValue(unitName_ar);
        return entity;
    }
    @ColumnName(key = "تاريخ البدء")
    public String getStartDateStr(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(startDate);
    }
    @ColumnName(key = "تاريخ الانتهاء")
    public String getEndDateStr(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(endDate);
    }
    @ColumnName(key = "التصنيف")
    public String getClassification(){
        String entity = Utils.removeNullValue(classification);
        return entity;
    }
    @ColumnName(key = "نسبة الإنجاز")
    public String getProgressStr(){
        String entity = progress +" %";
        return entity;
    }


}
