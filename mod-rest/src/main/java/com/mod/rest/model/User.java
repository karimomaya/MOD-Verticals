package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
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
    String title;
    String RoleName;
    String Phone;
    @NotFound(action = NotFoundAction.IGNORE)
    String facsimileTelephoneNumber;
    String Email;
    String notes;

    @ColumnName(key = "الاسم")
    public String getName() {
        return Utils.removeNullValue(DisplayName);
    }
    @ColumnName(key = "المنصب")
    public String getRole() {
        return Utils.removeNullValue(RoleName);
    }
    @ColumnName(key = "الرتبة")
    public String getTitle() {
        return Utils.removeNullValue(title);
    }
    @ColumnName(key = "رقم الهاتف")
    public String getPhone() {
        return Utils.removeNullValue(Phone);
    }
    @ColumnName(key = "رقم الجوال")
    public String getMobile() {
        return Utils.removeNullValue(facsimileTelephoneNumber);
    }
    @ColumnName(key = "الرقم العسكري")
    public String getMilitaryNumber() {
        return Utils.removeNullValue(notes);
    }
    @ColumnName(key = "البريد الالكتروني")
    public String getEmail() {
        return Utils.removeNullValue(Email);
    }
}
