package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
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
public class IPMainActivity {
    @Id
    Long mainActivityId;
    Date startDate;
    Date endDate;
    String activityName;
    String unitName_ar;
    String unitName_en;
    String progress;

    public String getActivityName(){

        return activityName ;
    }

    public String getProgress(){

        String progresss = Utils.removeNullValue(progress);
        if(progresss == null||progresss.length()==0) progresss = "0";
        return progresss;
    }
}
