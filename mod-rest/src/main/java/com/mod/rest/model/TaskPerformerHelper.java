package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by karim.omaya on 1/7/2020.
 */
@Getter
@Setter
@Entity
@Immutable
public class TaskPerformerHelper {
    @Id
    @Column(name="Id")
    long id;

    Date startDate;
    Date dueDate;
    Integer targetPerformerStatus;
    Integer taskStatus;
    String DisplayName;
    String taskName;
    int progress;

}
