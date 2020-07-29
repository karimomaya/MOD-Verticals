package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Immutable
public class CarSummary {
    @Id
    Long Id;

    @Column(name = "count")
    String count;
    @Column(name = "label")
    String label;

    @ColumnName(key = "الإجمالي")
    public String getCount (){return Utils.removeNullValue(count);}
    @ColumnName(key = "الحالة")
    public String getLabel () {return  Utils.removeNullValue(label);}
}
