package com.mod.rest.model;

import com.mod.rest.system.Utils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by omar.sabry on 12/9/2020.
 */
@Entity
@Data
@Table(name = "O2MyCompanyMeetingManagementMOD_MM_entity_Vacation")
public class Vacation {
    @Id
    @Column(name = "Id")
    long id;
    String name_ar;
    String name_eng;
    @Column(name = "[from]")
    Date fromDate;
    @Column(name = "[to]")
    Date toDate;
    @Column(name = "[rule]")
    String rule;
    Long parent_Id;


    public String getFromDate(){
        return Utils.convertDateToString(fromDate, "yyyy-MM-dd");
    }

    public String getToDate(){
        return Utils.convertDateToString(toDate, "yyyy-MM-dd");
    }
}
