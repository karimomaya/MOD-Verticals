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
public class CarMaintenanceReport {
    @Id
    Long Id;

    @Column(name = "accidents")
    String accidents;

    @Column (name = "maintenance")
    String maintenance;

    @Column (name = "plate_number")
    String plate_number;

    @ColumnName(key = "رقم اللوحة")
    public String getPlateNumber(){
        return Utils.removeNullValue(plate_number);
    }
    @ColumnName(key = "عدد الصيانات")
    public String getMaintenance() {
        return Utils.removeNullValue(maintenance);
    }
    @ColumnName(key = "عدد الحوادث")
    public String getAccidents () {
        return  Utils.removeNullValue(accidents);
    }
}
