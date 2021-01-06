package com.mod.rest.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by karim on 8/10/20.
 */


@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_position")
public class CountryLeaderPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Long countryLeaderPositionId;
    String position;
    Long position_to_leader_Id;
    String year;
    String month;
    String day;


    public String getDateStr() {
        return year + '-' + month + '-' + day;
    }
}
