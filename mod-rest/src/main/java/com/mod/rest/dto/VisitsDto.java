package com.mod.rest.dto;

import java.util.Date;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface VisitsDto {
     String getName();
     String getNotes();
     String getGoals();
     Date getDate();
     Long getVisitId();
}
