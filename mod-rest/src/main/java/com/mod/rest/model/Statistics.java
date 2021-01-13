package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by amira.sherif on 1/13/2020.
 */
@Getter
@Setter
@Entity

public class Statistics {
    @Id
    @Column(name="id")
    long id;
    int ended;
    int inProgress;
}
