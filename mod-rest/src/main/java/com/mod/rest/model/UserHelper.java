package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by karim.omaya on 12/24/2019.
 */
@Getter
@Setter
@Entity
@Immutable
public class UserHelper {
    @Id
    @Column(name="UserEntityId")
    long id;
    String DisplayName;
    String HeadUnit;
    long UnitId;
}
