package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by amira.sherif on 1/13/2021.
 */

@Entity
@Data
@Table(name = "O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_officialMissions")
public class OfficialMissionsDIA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    long id;
    String conclusion;
    Date dateOfBookDispatch;
    Date missionDate;
    String fieldBookNumber;
//    String headOfDelegation;
    String membersOfTheDelegation;
    String missionDescription;
    String notes;
    String place;
    String recommendations;
    String title;
    long officialMissions_to_countryFileBasic_Id;

    @OneToMany
    @JoinColumn(name = "parentEntityId")
    List<CountryAdditionalData> countryAdditionalData;

    @ManyToOne
    @JoinColumn(name = "headOfDelegation")
    @NotFound(action = NotFoundAction.IGNORE)
    User headOfDelegation;
    public String getheadOfDelegation(){
        User user = this.headOfDelegation;
        if (user != null) return user.getDisplayName();
        return "";
    }

}
