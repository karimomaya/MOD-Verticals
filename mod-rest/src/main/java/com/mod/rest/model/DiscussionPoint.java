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
        String result = "البند ";
        this.number =  result + convertPositionToWord(position);
    }

    public String getNumber(){
        return this.number;
    }

    public String convertPositionToWord(int position){
        switch (position){
            case 1:
                return "الأول";
            case 2:
                return "الثاني";
            case 3:
                return "الثالث";
            case 4:
                return "الرابع";
            case 5:
                return "الخامس";
            case 6:
                return "السادس";
            case 7:
                return "السابع";
            case 8:
                return "الثامن";
            case 9:
                return "التاسع";
            case 10:
                return "العاشر";
            case 11:
                return "الحادي عشر";
            case 12:
                return "الثاني عشر";
            default:
                return position+"";
        }
    }

}
