package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@Entity
@Data
@Table(name = "O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long Id;

    String type;
    String path;
    String parentItemId;

}
