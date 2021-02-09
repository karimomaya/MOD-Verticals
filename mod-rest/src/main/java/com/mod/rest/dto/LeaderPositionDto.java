package com.mod.rest.dto;

/**
 * Created by amira.sherif on 1/24/2021.
 */
public interface LeaderPositionDto {
     String getPosition();
     String getYear();
     String getMonth();
     String getDay();

     default String getDateStr() {
          return getYear() + '-' + getMonth() + '-' + getDay();
     }}
