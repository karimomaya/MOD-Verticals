package com.mod.rest.dto;

import java.util.Date;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface HumanAidDto {
     String getAssistanceProvided();
     String getReasonForAidOrEvent();
     String getTotalValueOfAid();
     String getNotes();
     String getType();
     Date getDate();
}
