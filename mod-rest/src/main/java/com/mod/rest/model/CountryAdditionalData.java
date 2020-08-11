package com.mod.rest.model;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by karim on 8/10/20.
 */
@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_additionalInformationData")
public class CountryAdditionalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long id;
    String title;
    String details;
    String type;
    String parentEntityId;
}
