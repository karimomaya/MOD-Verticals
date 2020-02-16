package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by karim on 2/16/20.
 */
@Entity
@Data
@Table(name = "O2MyCompanyGeneralSYS_GENERALMOD_MM_entity_DiscussionPoint")
public class DiscussionPoint {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long Id;

    String DiscussionPoint;
    Integer Priority;
    @Transient
    String number;

    public void setNumber(int position){
        String result = "بند ";
        this.number =  result + position;
    }

    public String getNumber(){
        return this.number;
    }

}
