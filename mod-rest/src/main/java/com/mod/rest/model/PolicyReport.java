package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.annotation.PDFResources;
import com.mod.rest.system.Utils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by karim on 6/23/20.
 */
@PDFResources(key="policy-report")
@Entity
public class PolicyReport {
    @Id
    Long BasicInfo_to_PolicyNew_Id;

    Integer complete_kpi;
    Integer total_kpi;
    String document_code;
    Integer mainIssueStatus;
    Integer policyPath;
    String policyDocumentName;
    String serial_num;
    String mainTarget;
    Date main_activity;

    @ColumnName(order = 1, key = "التسلسل")
    public String getSerialNumber() {
        String sn = Utils.removeNullValue(serial_num).trim();
        return sn.isEmpty()? "-" : sn ;
    }

    @ColumnName(order = 2, key = "اسم وثيقة السياسة")
    public String getTotal() {
        String sn = Utils.removeNullValue(policyDocumentName).trim();
        return sn.isEmpty()? "-" : sn ;
    }

    @ColumnName(order = 3, key = "رمز وثيقة السياسة")
    public String getDocumentCode() {
        String sn = Utils.removeNullValue(document_code).trim();
        return sn.isEmpty()? "-" : sn ;
    }

    @ColumnName(order = 4, key = "تاريخ تنفيذ السياسة")
    public String getExecutionDate() {
        String sn = Utils.dateFormat( main_activity, "YYYY-MM-dd");
        return sn.isEmpty()? "-" : sn ;
    }

    @ColumnName(order = 5, key = "الهدف من السياسة")
    public String getGoal() {
        String sn = Utils.removeNullValue(mainTarget).trim();
        return sn.isEmpty()? "-" : sn ;
    }

    @ColumnName(order = 6, key = "حالة الخطر الرئيسي")
    public String getmainIssueStatus() {
        if (mainIssueStatus == null) return "-";
        switch (mainIssueStatus) {
            case 1:
                return "إدارة الخطر بفعالية دون أن تؤثر على التنفيذ";
            case 2:
                return "أن الخطر يؤثر على الإلتزام بالجدول الزمني أو بفعالية التنفيذ";
            case 3:
                return "أن الخطر يتسبب في تأخير كبير أو يحول دون التنفيذ بفعالية";
            default:
                return "-";
        }
    }
    @ColumnName(order = 7, key = "هل السياسه في المسار الصحيح")
    public String getpolicyPath() {
        if (policyPath == null) return "-";
        switch (policyPath) {
            case 1:
                return "نعم";
            case 2:
                return "لا";
            default:
                return "-";
        }
    }

    @ColumnName(order = 8, key = "نسبة مؤشرات الأداء الرئيسية المكتملة")
    public int getCompleteKpi() {

        if (total_kpi == 0) return  0;

        int percentatge= (complete_kpi/total_kpi)*100;
        return percentatge;
    }
}
