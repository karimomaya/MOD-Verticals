package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by MinaSamir on 5/26/2020.
 */

@Entity
@Immutable
public class ProofreadingReport {
    @Id
    Long Id;
    @Column(name = "request_Department")
    String request_Department;
    @Column(name = "request_name")
    String request_name;
    @Column(name = "title")
    String title;
    @Column(name = "degreeOfConfidentiality")
    String degreeOfConfidentiality;
    @Column(name = "degreeOfPrecedence")
    String degreeOfPrecedence;
    @Column(name = "word_count")
    String word_count;
    @Column(name = "page_count")
    String page_count;
    @Column(name = "request_status")
    String request_status;
    @Column(name = "proofreader")
    String proofreader;
    @Column(name = "request_date")
    Date request_date;
    @Column(name = "proofreading_done_on")
    Date proofreading_done_on;
    @Column(name = "average_rating")
    String average_rating;
    @Column(name = "initiatorComments")
    String initiatorComments;


    @ColumnName(key = "رقم الطلب")
    public Long getId() {
        return Id;
    }

    @ColumnName(key = "جهة الطلب")
    public String getRequest_Department() {
        return request_Department;
    }

    public void setRequest_Department(String request_Department) {
        this.request_Department = request_Department;
    }

    @ColumnName(key = "اسم مقدم الطلب")
    public String getRequest_name() {
        return request_name;
    }

    @ColumnName(key = "الموضوع")
    public String getTitle() {
        return title;
    }

    @ColumnName(key = "درجة السرية")
    public String getDegreeOfConfidentiality() {
        return degreeOfConfidentiality;
    }

    public void setDegreeOfConfidentiality(String degreeOfConfidentiality) {
        this.degreeOfConfidentiality = degreeOfConfidentiality;
    }

    @ColumnName(key = "درجة الأسبقية")
    public String getDegreeOfPrecedence() {
        return degreeOfPrecedence;
    }

    public void setDegreeOfPrecedence(String degreeOfPrecedence) {
        this.degreeOfPrecedence = degreeOfPrecedence;
    }

    @ColumnName(key = "عدد الكلمات")
    public String getWord_count() {
        return word_count;
    }

    @ColumnName(key = "عدد الصفحات")
    public String getPage_count() {
        return page_count;
    }

    @ColumnName(key = "حالة الطلب")
    public String getRequest_status() {
        return request_status;
    }

    @ColumnName(key = "المدقق")
    public String getProofreader() {
        return proofreader;
    }

    @ColumnName(key = "تاريخ الطلب")
    public Date getRequest_date() {
        return request_date;
    }

    @ColumnName(key = "تاريخ التسليم")
    public Date getProofreading_done_on() {
        return proofreading_done_on;
    }

    @ColumnName(key = "التقييم")
    public String getAverage_rating() {
        return average_rating;
    }

    @ColumnName(key = "ملاحظات الجهة الطالبة")
    public String getInitiatorComments() {
        return initiatorComments;
    }
}