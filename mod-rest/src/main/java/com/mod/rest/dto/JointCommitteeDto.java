package com.mod.rest.dto;

import java.util.Date;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface JointCommitteeDto {
     String getCommitteeName();
     String getCommitteeNumber();
     String getCommitteeType();
     String getCountryName();
     String getNotes();
     Date getDate();
     String getCommitteeTypeValue();
     Long getJointCommitteeId();
}
