package com.mod.soap.dao.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by karim.omaya on 1/12/2020.
 */
@Entity
@Data
@Table(name = "O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_security")
public class Security {
    @Id
    long id;
    String code;
    String target;
    int type; // 1:unit type code, 2: unit code,3: role name
}
