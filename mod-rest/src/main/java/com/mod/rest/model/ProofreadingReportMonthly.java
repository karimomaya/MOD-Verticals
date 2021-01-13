package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by MinaSamir on 5/27/2020.
 */

@Entity

public class ProofreadingReportMonthly {
    @Id
    String request_Department;
    @Column(name = "word_count")
    String wordCount;
    @Column(name = "page_count")
    String pageCount;
    @Column(name = "number_of_requests")
    String numberOfRequests;

    @ColumnName(order = 1, key = "جهة الطلب")
    public String getRequest_Department() {
        return request_Department;
    }

    public void setRequest_Department(String request_Department) {
        this.request_Department = request_Department;
    }

    @ColumnName(order = 2, key = "عدد الكلمات")
    public String getWordCount() {
        return wordCount;
    }

    @ColumnName(order = 3, key = "عدد الصفحات")
    public String getPageCount() {
        return pageCount;
    }

    @ColumnName(order = 4, key = "عدد الطلبات")
    public String getNumberOfRequests() {
        return numberOfRequests;
    }
}