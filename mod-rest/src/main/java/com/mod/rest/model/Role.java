package com.mod.rest.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by MinaSamir on 5/27/2020.
 */

@Entity

public class Role {
    @Id
    Long Id;
//    @Column(name="RoleName")
//    String RoleName;
//    @Column(name="UnitId")
//    Integer UnitId;
//    @Column(name="IsHeadRole")
//    Boolean IsHeadRole;
//    @Column(name="HasDirectorateAuthority")
//    Boolean HasDirectorateAuthority;
//    @Column(name="IsUnitOwner")
//    String IsUnitOwner;
    @Column(name="roleCode")
    String roleCode;
    @Column(name="RoleName_ar")
    String RoleName_ar;
    @Column(name="RoleName_en")
    String RoleName_en;

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleNameByLanguage(String lang) {
        if (lang.equals("ar"))
            return RoleName_ar;
        else return RoleName_en;
    }
}
