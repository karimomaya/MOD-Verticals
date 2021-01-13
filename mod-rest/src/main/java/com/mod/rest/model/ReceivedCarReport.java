package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data

public class ReceivedCarReport {
    @Id
    Long Id;
    @Column(name = "plate_number")
    String plateNumber;

    @Column(name = "model")
    String model;

    @Column(name = "type")
    String type;

    @Column(name = "color")
    String color;

    @Column(name = "date_required_to_receive_the_vehicle")
    Date dateRequiredToReceiveVehicle;

    @Column(name = "effectiveDate")
    String effectiveDeliveryDate;

//    @Column (name = "status")
//    String status;

    @ColumnName(order = 1, key = "رقم اللوحة")
    public String getPlateNumber (){
        return Utils.removeNullValue(plateNumber);
    }
    @ColumnName(order = 2, key = "الموديل")
    public String getModel (){
        return Utils.removeNullValue(model);
    }

    @ColumnName(order = 3, key = "التصنيف")
    public String getType (){
        return Utils.removeNullValue(type);
    }

    @ColumnName(order = 4, key = "اللون")
    public String getColor (){
        return Utils.removeNullValue(color);
    }
//    @ColumnName(order = 1, key = "الحالة")
//    public String getStatus (){
//        return Utils.removeNullValue(status);
//    }
    @ColumnName(order = 5, key = "تاريخ التوفير")
    public String getDateRequiredToReceiveVehicle(){
        if(dateRequiredToReceiveVehicle == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateRequiredToReceiveVehicle);
    }
    @ColumnName(order = 6, key = "تاريخ التسليم")
    public String getEffectiveDeliveryDate(){
        if(effectiveDeliveryDate == null){
            return "_";
        }
        return Utils.removeNullValue(effectiveDeliveryDate);
    }
}
