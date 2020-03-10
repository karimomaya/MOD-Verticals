package com.mod.rest.model;

import com.mod.rest.annotation.PDFResources;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by omar.sabry on 3/10/2020.
 */
@Getter
@Setter
@Entity
@Table(name = "O2MyCompanyGovernmentCommunicationDivisionGCDMOD_GCD_entity_design_seals")
@PDFResources(key="design-seal-template")
public class DesignSeal {
    @Id
    long Id;
    Integer request_type;
    String main_unit_or_sub_ar;
    String main_unit_or_sub_en;
    Boolean red_color;
    Boolean green_color;
    String notes;

    public String getColors(){
        if(red_color && green_color){
            return "أحمر, أخضر";
        }else if(red_color){
            return "أحمر";
        }else if(green_color){
            return "أخضر";
        }
        return "";
    }

    public String getRequestType(){
        switch (request_type){
            case 1:
                return "جديد";
            case 2:
                return "تعديل مسمي المديرية";
            default:
                return "";
        }
    }
}
