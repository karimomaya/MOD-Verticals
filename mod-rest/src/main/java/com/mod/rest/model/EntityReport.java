package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by omar.sabry on 1/9/2020.
 */

@Entity
@Table(name = "O2MyCompanyContactTrackerMOD_CT_entity_Entity")
public class EntityReport {
    @Id
    Long Id;
    String nameArabic;
    String nameEnglish;
    String address;
    String phone;
    String fax;
    String postalNumber;
    String facebookUrl;
    String twitterUrl;
    String mainActivity;
    String website;
    String executiveOfficer;
    String owner;
    Integer type;
    Boolean isMainBranch;
    String mainBranchName;
    Boolean isRegistered;
    String notes;

    @ColumnName(order = 1, key = "اسم الجهة بالعربية")
    public String getNameArabic(){
        return Utils.removeNullValue(nameArabic);
    }

    @ColumnName(order = 2, key = "اسم الجهة بالانجليزية")
    public String getNameEnglish(){
        return Utils.removeNullValue(nameEnglish);
    }

    @ColumnName(order = 3, key = "عنوان الجهة")
    public String getAddress(){
        return Utils.removeNullValue(address);
    }


    @ColumnName(order = 4, key = "نوع الجهة")
    public String getType() {
        switch (type){
            case 1:
                return "حكومية اتحادية";
            case 2:
                return "حكومية محلية";
            case 3:
                return "شبه حكومية";
            case 5:
                return "هيئة دبلوماسية";
            case 4:
                return "قطاع خاص";
            case 6:
                return "أخرى";
        }
        return "حكومية";
    }

    @ColumnName(order = 5, key = "الفرع")
    public String getMainBranch() {
        if (isMainBranch){
            return "الرئيسي";
        }else{
            return "غير الرئيسي";
        }
    }

    @ColumnName(order = 6, key = "اسم الجهة الرئيسية")
    public String getMainBranchName() {
        return Utils.removeNullValue(mainBranchName);
    }

    @ColumnName(order = 7, key = "رقم الهاتف")
    public String getPhone(){
        return Utils.removeNullValue(phone);
    }

    @ColumnName(order = 8, key = "رقم الفاكس")
    public String getFax(){
        return Utils.removeNullValue(fax);
    }

    @ColumnName(order = 9, key = "صندوق البريد")
    public String getPostalNumber(){
        return Utils.removeNullValue(postalNumber);
    }

    @ColumnName(order = 10, key = "الموقع الإلكتروني")
    public String getWebsite() {
        return Utils.removeNullValue(website);
    }

    @ColumnName(order = 11, key = "موقع التواصل الإجتماعي (Facebook)")
    public String getFacebookUrl() {
        return Utils.removeNullValue(facebookUrl);
    }

    @ColumnName(order = 12, key = "موقع التواصل الإجتماعي (Twitter)موقع التواصل الإجتماعي (Twitter)")
    public String getTwitterUrl() {
        return Utils.removeNullValue(twitterUrl);
    }

    @ColumnName(order = 13, key = "النشاط الرئيسي")
    public String getMainActivity() {
        return Utils.removeNullValue(mainActivity);
    }

    @ColumnName(order = 14, key = "الملاحظات")
    public String getNotes() {
        return Utils.removeNullValue(notes);
    }

    @ColumnName(order = 15, key = "التسجيل في سجل الموردين")
    public String getRegistered() {
        if(type == 4){
            if(isRegistered){
                return "نعم";
            }else{
                return "لا";
            }
        }else{
            return "";
        }
    }

    @ColumnName(order = 16, key = "المدير التنفيذى")
    public String getExecutiveOfficer() {
        return Utils.removeNullValue(executiveOfficer);
    }

    @ColumnName(order = 17, key = "المالك")
    public String getOwner() {
        return Utils.removeNullValue(owner);
    }
}
