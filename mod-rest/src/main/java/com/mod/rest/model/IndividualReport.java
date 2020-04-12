package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by omar.sabry on 1/9/2020.
 */

@Entity
@Table(name = "O2MyCompanyContactTrackerMOD_CT_entity_Individual")
public class IndividualReport {
    @javax.persistence.Id
    Long Id;
    String nameArabic;
    String nameEnglish;
    String positionArabic;
    String positionEnglish;
    String phone;
    String mobile;
    String email;
    String nationality;
    Date securityStartDate;
    Date securityEndDate;
    String notes;
    String entityName;
    String securityClearanceNumber;
    String identificationNumber;
    String unifiedNumber;

    @ColumnName(key = "الاسم باللغة العربية")
    public String getNameArabic() {
        return Utils.removeNullValue(nameArabic);
    }

    @ColumnName(key = "الاسم باللغة الإنجليزية")
    public String getNameEnglish() {
        return Utils.removeNullValue(nameEnglish);
    }

    @ColumnName(key = "المنصب باللغة العربية")
    public String getPositionArabic() {
        return Utils.removeNullValue(positionArabic);
    }

    @ColumnName(key = "المنصب باللغة الإنجليزية")
    public String getPositionEnglish() {
        return Utils.removeNullValue(positionEnglish);
    }

    @ColumnName(key = "اسم الجهة")
    public String getEntityName() {
        return Utils.removeNullValue(entityName);
    }

    @ColumnName(key = "الجنسية")
    public String getNationality() {
        return Utils.removeNullValue(nationality);
    }

    @ColumnName(key = "قم الهاتف الداخلي")
    public String getPhone() {
        return Utils.removeNullValue(phone);
    }

    @ColumnName(key = "رقم الهاتف الجوال")
    public String getMobile() {
        return Utils.removeNullValue(mobile);
    }

    @ColumnName(key = "البريد الإلكتروني")
    public String getEmail() {
        return Utils.removeNullValue(email);
    }

    @ColumnName(key = "رقم الهوية")
    public String getIdentificationNumber() {
        return Utils.removeNullValue(identificationNumber);
    }

    @ColumnName(key = "الرقم الموحد")
    public String getUnifiedNumber() {
        return Utils.removeNullValue(unifiedNumber);
    }

    @ColumnName(key = "رقم التصريح الأمنى")
    public String getSecurityClearanceNumber() {
        return Utils.removeNullValue(securityClearanceNumber);
    }

    @ColumnName(key = "تاريخ بداية التصريح")
    public String getSecurityStartDate() {
        if(securityStartDate == null){
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(securityStartDate);
    }

    @ColumnName(key = "تاريخ نهاية التصريح")
    public String getSecurityEndDate() {
        if(securityEndDate == null){
            return "";
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(securityEndDate);
    }

    @ColumnName(key = "الملاحظات")
    public String getNotes() {
        return Utils.removeNullValue(notes);
    }
}
