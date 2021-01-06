package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abdallah.shaaban on 9/1/2020.
 */
@Entity
@Immutable
@Data
public class InstitutionalSupportHousingUnitEvacuationReport {
    @Id
    Long Id;
    Date dataOfReceiptOfHousingUnit;
    Date dateOfDeliveryOfHousingUnit;
    String Discount_of_insuranceValue;
    String flatNumber;
    String flatType;
    String houingUnitNumber;
    String housingUnitFloorNumber;
    String nameOfLastResident;
    String militaryNumber;
    String res_Rank;

    @ColumnName(order = 1, key = "رقم البناية")
    public String getHouingUnitNumber() {
        String entity = Utils.removeNullValue(houingUnitNumber);
        return entity;
    }

    @ColumnName(order = 2, key = "رقم الطابق")
    public String getHousingUnitFloorNumber() {
        String entity = Utils.removeNullValue(housingUnitFloorNumber);
        return entity;
    }

    @ColumnName(order = 3, key = "رقم الوحدة السكنية")
    public String getFlatNumber() {
        String entity = Utils.removeNullValue(flatNumber);
        return entity;
    }

    @ColumnName(order = 4, key = "نوع الوحدة السكنية")
    public String getFlatType() {
        String entity = Utils.removeNullValue(flatType);
        return entity;
    }



    @ColumnName(order = 5, key = "تاريخ إستلام الوحدة")
    public String getDataOfReceiptOfHousingUnit() {
        if (dataOfReceiptOfHousingUnit == null) {
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dataOfReceiptOfHousingUnit);
    }

    @ColumnName(order = 6, key = "تاريخ تسليم الوحدة")
    public String getDateOfDeliveryOfHousingUnit() {
        if (dateOfDeliveryOfHousingUnit == null) {
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateOfDeliveryOfHousingUnit);
    }

    @ColumnName(order = 7, key = "الرقم العسكري/الوظيفي")
    public String getMilitaryNumber() {
        String entity = Utils.removeNullValue(militaryNumber);
        return entity;
    }

    @ColumnName(order = 8, key = "اسم آخر ساكن")
    public String getNameOfLastResident() {
        String entity = Utils.removeNullValue(nameOfLastResident);
        return entity;
    }

    @ColumnName(order = 9, key = "الرتبة")
    public String getRes_Rank() {
        String entity = Utils.removeNullValue(res_Rank);
        return entity;
    }

    @ColumnName(order = 10, key = "قيمة الخصم من مبلغ التأمين (د.إ)")
    public String getComments() {
        String entity = Utils.removeNullValue(Discount_of_insuranceValue);
        return entity;
    }

}
