package com.mod.soap.dao.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by omar.sabry on 3/16/2020.
 */
@Entity
@Data
@Table(name = "O2MyCompanyContactTrackerMOD_CT_entity_Individual")
public class ExternalUser {
    @Id
    Long Id;
    String nameArabic;
    String nameEnglish;
    String positionArabic;
    String positionEnglish;
    String phone;
    String mobile;
    String email;
    String nationality;
    Date securityStartDate;
    Date securityEndDate;
    String notes;
    String entityName;
    String securityClearanceNumber;
    String identificationNumber;
    String unifiedNumber;
}
