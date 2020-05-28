package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by MinaSamir on 5/27/2020.
 */

@Entity
@Immutable
public class ProofreadingReportMonthly {
    @Id
    String request_Department;
    @Column(name = "word_count")
    String word_count;
    @Column(name = "page_count")
    String page_count;
    @Column(name = "number_of_requests")
    String number_of_requests;

    @ColumnName(key = "جهة الطلب")
    public String getRequest_Department() {
        return request_Department;
    }

    public void setRequest_Department(String request_Department) {
        this.request_Department = request_Department;
    }

    @ColumnName(key = "عدد الكلمات")
    public String getWord_count() {
        return word_count;
    }

    @ColumnName(key = "عدد الصفحات")
    public String getPage_count() {
        return page_count;
    }

    @ColumnName(key = "عدد الطلبات")
    public String getNumber_of_requests() {
        return number_of_requests;
    }
}