package com.mod.soap.dao.model;

import com.mod.soap.dao.repository.UserRepository;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by omar.sabry on 9/6/2020.
 */
@Entity
@Data
@Immutable
@Table(name = "MOD_SYS_GENERAL_DB_TaskRG_V")

public class NotificationTask {
    @Id
    String TASK_INSTANCE_ID;

    String AssignedRoleName;
    String TARGET_TYPE;
    String SenderName;
    String SUBJECT;
    String TASKOWNER;
}
