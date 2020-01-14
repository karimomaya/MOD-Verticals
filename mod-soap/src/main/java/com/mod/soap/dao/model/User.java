package com.mod.soap.dao.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by karim.omaya on 12/15/2019.
 */
@Entity
@Data
@Table(name = "MOD_SYS_OC_DB_Role_User_V")
public class User {
    @Id
    @Column(name="UserEntityId")
    long id;
    String username;
    String DisplayName;
    String RoleName;
    String UnitTypeCode;
    String UnitCode;

}
