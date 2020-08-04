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

    @ColumnName(key = "التسلسل")
    public String getSerialNumber() { return Utils.removeNullValue(serial_num); }

    @ColumnName(key = "اسم وثيقة السياسة")
    public String getTotal() { return policyDocumentName; }

    @ColumnName(key = "رمز وثيقة السياسة")
    public String getDocumentCode() { return Utils.removeNullValue(document_code); }

    @ColumnName(key = "تاريخ تنفيذ السياسة")
    public String getExecutionDate() { return Utils.dateFormat( main_activity, "YYYY-MM-dd")  ; }

    @ColumnName(key = "الهدف من السياسة")
    public String getGoal() { return Utils.removeNullValue(mainTarget); }

    @ColumnName(key = "حالة الخطر الرئيسي")
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
    @ColumnName(key = "هل السياسه في المسار الصحيح")
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

    @ColumnName(key = "نسبة مؤشرات الأداء الرئيسية المكتملة")
    public int getCompleteKpi() {

        if (total_kpi == 0) return  0;

        int percentatge= (complete_kpi/total_kpi)*100;
        return percentatge;
    }
}
