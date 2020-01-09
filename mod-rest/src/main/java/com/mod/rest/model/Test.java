package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.annotation.PDFResources;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@Getter
@Setter
@PDFResources(key="meeting-template")
public class Test {
    String name;
    int Id;
    Date startDate;
}
