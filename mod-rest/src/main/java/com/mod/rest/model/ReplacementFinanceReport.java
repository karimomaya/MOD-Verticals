package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 7/21/2020.
 */

@Entity

public class ReplacementFinanceReport {
    @Id
    Long Id;
    @Column(name="DIR")
    String DIR;
    @Column(name="UnitName")
    String unitName;
    @Column(name="date_of_replacement")
    Date dateOfReplacement;
    @Column(name="sum_of_imprest")
    String sumOfImprest;
    @Column(name="amount_spent")
    String amountSpent;
    @Column(name="check_number")
    String checkNumber;
    @Column(name="date_of_issuance_of_the_check")
    String dateOfIssuanceOfTheCheck;
    @Column(name="total_amount")
    String totalAmount;
    @Column(name="total_amount_monthly")
    String totalAmountMonthly;
    @Column(name="currentAmount")
    String currentAmount;


    @ColumnName(order = 1, key = "الرمز")
    public String getDIR(){
        return Utils.removeNullValue(DIR);
    }

    @ColumnName(order = 2, key = "اسم الجهة")
    public String getUnitName(){
        return Utils.removeNullValue(unitName);
    }

    @ColumnName(order = 3, key = "التاريخ")
    public Date getDateOfReplacement(){
        return dateOfReplacement;
    }

    @ColumnName(order = 4, key = "المصروف")
    public String getSumOfImprest(){
        return Utils.removeNullValue(sumOfImprest);
    }



    @ColumnName(order = 5, key = "الإجمالي")
    public String getAmountSpent(){
        return Utils.removeNullValue(amountSpent);
    }
    @ColumnName(order = 6, key = "رقم الشيك/رقم التحويل")
    public String getCheckNumber(){
        return Utils.removeNullValue(checkNumber);
    }
    @ColumnName(order = 7, key = "تاريخ إصدار الشيك/التحويل")
    public String getDateOfIssuanceOfTheCheck(){
        return dateOfIssuanceOfTheCheck;
    }
    @ColumnName(order = 8, key = "حد المبلغ السنوي")
    public String getTotalAmount(){
        return Utils.removeNullValue(totalAmount);
    }
    @ColumnName(order = 9, key = "الرصيد السنوي")
    public String getCurrentAmount(){
        return Utils.removeNullValue(currentAmount);
    }

    @ColumnName(order = 10, key = "حد المبلغ الشهري")
    public String getTotalAmountMonthly(){
        return Utils.removeNullValue(totalAmountMonthly);
    }



}