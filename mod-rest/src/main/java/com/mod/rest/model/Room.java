package com.mod.rest.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@Entity
@Data
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_room")
public class Room {

    @Id
    long Id;

    String roomName;


}
