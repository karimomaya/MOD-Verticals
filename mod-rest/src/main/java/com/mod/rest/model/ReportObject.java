package com.mod.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Getter
@Setter
public class ReportObject {
    int reportType;
    int detectedReportType = 0; // 0: graph, 1: table, 2: count, 3: file
    int status;
    long[] risks;
    long[] projects;
    long[] users;
    Date startDate;
    Date endDate;
    String riskIds;
    String projectIds;
    String userIds = ";";
    int pageNumber = 0;
    int pageSize = 0;
    String SAMLart;
    int type;
    String nameArabic;
    String nameEnglish;
    String phone;
    String tags;
    String sortBy;
    String sortDir;
    int entityType;
    int isRegistered;
    String licenseNumber;
    String entityName;
    String name;
    String position;

    public ReportObject setSAMLart(String SAMLart){
        this.SAMLart = SAMLart;
        return this;
    }

    public ReportObject build(){
        userIds = ";";
        for (int i=0; users != null && i< users.length;i++){
            userIds += users[i]+";";
        }
        if (reportType == 6){
            riskIds = "";
            for (int i=0; risks !=null && i< risks.length;i++){
                if(i > 0) riskIds += ",";
                riskIds += risks[i];
            }

        } else if (reportType == 4){
            projectIds= "";
            for (int i=0; projects !=null && i< projects.length;i++){
                if(i > 0) projectIds += ",";
                projectIds += projects[i];
            }
        }

        if (detectedReportType == 0 && pageNumber == 0){
            pageNumber = 1;
        } else if(detectedReportType == 0 && pageNumber != 0){
            detectedReportType = 1;
        }

        return this;
    }

    public ReportObject changeDetectedReportType(int definedType){
        this.detectedReportType = definedType;
        return this;
    }


}
