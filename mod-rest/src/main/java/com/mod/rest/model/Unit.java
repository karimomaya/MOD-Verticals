package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by MinaSamir on 5/27/2020.
 */

@Entity
@Immutable
public class Unit {
    @Id
    Long Id;
//    @Column(name="UnitName")
//    String UnitName;
//    @Column(name="UnitPathById")
//    String UnitPathById;
//    @Column(name="ParentUnitId")
//    Integer ParentUnitId;
//    @Column(name="UnitType")
//    String UnitType;
//    @Column(name="UnitManagerRoleId")
//    Integer UnitManagerRoleId;
//    @Column(name="UnitTypeCode")
//    String UnitTypeCode;
    @Column(name="UnitCode")
    String UnitCode;
//    @Column(name="UnitOwnerRoleId")
//    String UnitOwnerRoleId;
//    @Column(name="UnitTypeCodeId")
//    String UnitTypeCodeId;
//    @Column(name="UnitSerial")
//    String UnitSerial;
    @Column(name="UnitName_ar")
    String UnitName_ar;
    @Column(name="UnitName_en")
    String UnitName_en;

    public String getUnitCode() {
        return UnitCode;
    }

    public String getUnitNameByLanguage(String lang) {
        if (lang.equals("ar"))
            return UnitName_ar;
        else return UnitName_en;
    }
}
