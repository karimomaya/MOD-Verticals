package com.mod.soap.dao.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

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
    @Column(name="UserId")
    String username;
    String DisplayName;
    String RoleName;
    String UserUnitTypeCode;
    @NotFound(action = NotFoundAction.IGNORE)
    String UserUnitCode;
    @NotFound(action = NotFoundAction.IGNORE)
    String roleCode;
    @Transient
    String ticket;
}
