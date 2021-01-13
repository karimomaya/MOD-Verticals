package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data

public class CarMaintenanceReport {
    @Id
    Long Id;

    @Column(name = "accidents")
    String accidents;

    @Column (name = "maintenance")
    String maintenance;

    @Column (name = "plate_number")
    String plate_number;

    @ColumnName(order = 1, key = "رقم اللوحة")
    public String getPlateNumber(){
        return Utils.removeNullValue(plate_number);
    }
    @ColumnName(order = 2, key = "عدد الصيانات")
    public String getMaintenance() {
        return Utils.removeNullValue(maintenance);
    }
    @ColumnName(order = 3, key = "عدد الحوادث")
    public String getAccidents () {
        return  Utils.removeNullValue(accidents);
    }
}
