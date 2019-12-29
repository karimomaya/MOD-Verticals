package com.mod.soap.dao.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by karim.omaya on 12/15/2019.
 */
@Entity
@Data
@Table(name = "O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_config")
public class Config {
    @Id
    @Column(name="Id")
    long id;
    String category;
    String value;

}
