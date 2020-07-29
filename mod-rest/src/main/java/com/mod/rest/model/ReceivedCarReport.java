package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
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
@Immutable

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

    @ColumnName(key = "رقم اللوحة")
    public String getPlateNumber (){
        return Utils.removeNullValue(plateNumber);
    }
    @ColumnName(key = "الموديل")
    public String getModel (){
        return Utils.removeNullValue(model);
    }

    @ColumnName(key = "التصنيف")
    public String getType (){
        return Utils.removeNullValue(type);
    }

    @ColumnName(key = "اللون")
    public String getColor (){
        return Utils.removeNullValue(color);
    }
//    @ColumnName(key = "الحالة")
//    public String getStatus (){
//        return Utils.removeNullValue(status);
//    }
    @ColumnName(key = "تاريخ التوفير")
    public String getDateRequiredToReceiveVehicle(){
        if(dateRequiredToReceiveVehicle == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateRequiredToReceiveVehicle);
    }
    @ColumnName(key = "تاريخ التسليم")
    public String getEffectiveDeliveryDate(){
        if(effectiveDeliveryDate == null){
            return "NA";
        }
        return Utils.removeNullValue(effectiveDeliveryDate);
    }
}
