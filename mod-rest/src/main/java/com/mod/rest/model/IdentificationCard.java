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
@Table(name = "O2MyCompanyGovernmentCommunicationDivisionGCDMOD_GCD_entity_design_of_identity_card")
@PDFResources(key="identification-card-template")
public class IdentificationCard {
    @Id
    long Id;
    String emp;
    String emp_name_ar;
    String emp_name_en;
    String administration_ar;
    String administration_en;
    String job_title_ar;
    String job_title_en;
    String email_adress;
    String landline_phone;
    String mobile_number;
    String fax_number;
    String mail_box_number;
    String notes;

    public String getEmp(){
        if(emp != null){
            return emp.split(",")[0];
        }
        return "";
    }
}
