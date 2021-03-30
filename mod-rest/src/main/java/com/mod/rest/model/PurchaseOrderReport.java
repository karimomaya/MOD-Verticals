package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;


import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by omar.sabry on 4/19/2020.
 */

@Entity

public class PurchaseOrderReport {
    @Id
    Long POID;
    @Column(name="entity_beneficiary")
    String entity_beneficiary;
    @Column(name="requesttype")
    String requesttype;
    @Column(name="purchaseOrderNumber")
    String purchaseOrderNumber;
    @Column(name="PODate")
    Date PODate;
    @Column(name="reqeustNumber")
    String reqeustNumber;
    @Column(name="requestDate")
    Date requestDate;
    @Column(name="amount")
    String amount;

    @ColumnName(order = 1, key = "الجهة")
    public String getEntityBeneficiary(){
        String entity = Utils.removeNullValue(entity_beneficiary);
        entity = entity.split(",")[0];
        return entity;
    }

    @ColumnName(order = 2, key = "النوع (عقد/أمر شراء)")
    public String getRequestType(){
        switch (requesttype){
            case "PO":
                return "أمر شراء";
            case "contract":
                return "عقد";
        }
        return Utils.removeNullValue(requesttype);
    }

    @ColumnName(order = 3, key = "رقم طلب الشراء")
    public String getPurchaseOrderNumber(){
        return Utils.removeNullValue(purchaseOrderNumber);
    }

    @ColumnName(order = 4, key = "تاريخ طلب الشراء")
    public String getPODate() {
        if(PODate == null){
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(PODate);
    }

    @ColumnName(order = 5, key = "رقم العقد / أمر الشراء")
    public String getRequestNumber(){
        return Utils.removeNullValue(reqeustNumber);
    }

    @ColumnName(order = 6, key = "تاريخ العقد / أمر الشراء")
    public String getRequestDate() {
        if(requestDate == null){
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(requestDate);
    }

    @ColumnName(order = 7, key = "القيمة")
    public String getAmount(){
        return Utils.removeNullValue(amount);
    }
}