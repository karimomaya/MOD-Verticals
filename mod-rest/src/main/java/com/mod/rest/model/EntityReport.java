package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;

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

    @ColumnName(key = "اسم الجهة بالعربية")
    public String getNameArabic(){
        return removeNullValue(nameArabic);
    }

    @ColumnName(key = "اسم الجهة بالانجليزية")
    public String getNameEnglish(){
        return removeNullValue(nameEnglish);
    }

    @ColumnName(key = "عنوان الجهة")
    public String getAddress(){
        return removeNullValue(address);
    }

    @ColumnName(key = "نوع الجهة")
    public String getType() {
        switch (type){
            case 1:
                return "حكومية";
            case 2:
                return "شبه حكومية";
            case 3:
                return "هيئة دبلوماسية";
            case 4:
                return "قطاع خاص";
            case 5:
                return "أخرى";
        }
        return "حكومية";
    }

    @ColumnName(key = "الفرع")
    public String getMainBranch() {
        if (isMainBranch){
            return "الرئيسي";
        }else{
            return "غير الرئيسي";
        }
    }

    @ColumnName(key = "اسم الجهة الرئيسية")
    public String getMainBranchName() {
        return removeNullValue(mainBranchName);
    }

    @ColumnName(key = "رقم الهاتف")
    public String getPhone(){
        return removeNullValue(phone);
    }

    @ColumnName(key = "رقم الفاكس")
    public String getFax(){
        return removeNullValue(fax);
    }

    @ColumnName(key = "صندوق البريد")
    public String getPostalNumber(){
        return removeNullValue(postalNumber);
    }

    @ColumnName(key = "الموقع الإلكتروني")
    public String getWebsite() {
        return removeNullValue(website);
    }

    @ColumnName(key = "موقع التواصل الإجتماعي (Facebook)")
    public String getFacebookUrl() {
        return removeNullValue(facebookUrl);
    }

    @ColumnName(key = "موقع التواصل الإجتماعي (Twitter)موقع التواصل الإجتماعي (Twitter)")
    public String getTwitterUrl() {
        return removeNullValue(twitterUrl);
    }

    @ColumnName(key = "النشاط الرئيسي")
    public String getMainActivity() {
        return removeNullValue(mainActivity);
    }

    @ColumnName(key = "الملاحظات")
    public String getNotes() {
        return removeNullValue(notes);
    }

    @ColumnName(key = "التسجيل في سجل الموردين")
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

    @ColumnName(key = "المدير التنفيذى")
    public String getExecutiveOfficer() {
        return removeNullValue(executiveOfficer);
    }

    @ColumnName(key = "المالك")
    public String getOwner() {
        return removeNullValue(owner);
    }

    private String removeNullValue(String value){
        if (value == null) return "";
        if (value.equals("null")){
            return "";
        }
        return value;
    }
}
