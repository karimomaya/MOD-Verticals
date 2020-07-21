package com.mod.rest.model;


import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Date;

/**
 * Created by abdallah.shaaban on 7/19/2020.
 */

@Entity
@Immutable
@Data
public class IPSubActivity {
    @Id
    Date startDate;
    Date endDate;
    String subActivityName;
    String unitName_ar;
    String unitName_en;
    Integer progress;

}
