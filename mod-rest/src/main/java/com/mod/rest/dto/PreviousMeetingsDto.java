package com.mod.rest.dto;

import java.util.Date;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface PreviousMeetingsDto {
     Long getMeetingId();
     String getMeetingTitle();
     String getPlace();
     Date getDate();
}
