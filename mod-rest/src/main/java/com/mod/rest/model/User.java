package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by karim.omaya on 10/31/2019.
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "MOD_SYS_OC_DB_Role_User_V")
public class User implements Serializable {
    @Id
    @Column(name="UserEntityId")
    long id;
    @NotFound(action = NotFoundAction.IGNORE)
    String username;
    String DisplayName;

}
