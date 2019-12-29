package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by karim.omaya on 11/1/2019.
 */

@NamedStoredProcedureQuery(
        name="MOD_TM_SP_UserProductivityReport",
        procedureName="MOD_TM_SP_UserProductivityReport",
        resultClasses = { UserProductivitySP.class },
        parameters={
                @StoredProcedureParameter(name="StartDate", type=Date.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="EndDate", type=Date.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="List", type=String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="PageNumber", type=Integer.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="PageSize", type=Integer.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="TaskOwner", type=Integer.class, mode=ParameterMode.IN)
        }
)
@Setter
@Getter
@Entity
public class UserProductivitySP implements Serializable {

    @Id
    long Id;

    String taskName;
    int owner;
    int createdBy;
    Date dueDate;
    Date startDate;
    String priority;
    String status; //0:stopped, 1: not started, 2: started, 3: finished 10: draft
    String description;
    @ManyToOne
    @JoinColumn(name = "taskProjectId")
    @NotFound(action= NotFoundAction.IGNORE)
    Project project;
    /*long taskProjectId;*/
    Boolean singleOrMultiple;
    Integer progress;
    String entityBWSId;
    long performerId;
    /*@OneToMany( mappedBy="task", fetch = FetchType.EAGER)
    List<TaskPerformer> taskPerformers;*/
    @Transient
    User userCreatedBy;
    @Transient
    User userOwner;
}
