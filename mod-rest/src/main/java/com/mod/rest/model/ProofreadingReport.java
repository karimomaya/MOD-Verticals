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
    String requestDepartment;
    @Column(name = "request_name")
    String requestName;
    @Column(name = "title")
    String title;
    @Column(name = "degreeOfConfidentiality")
    String degreeOfConfidentiality;
    @Column(name = "degreeOfPrecedence")
    String degreeOfPrecedence;
    @Column(name = "word_count")
    String wordCount;
    @Column(name = "page_count")
    String pageCount;
    @Column(name = "request_status")
    String requestStatus;
    @Column(name = "proofreader")
    String proofreader;
    @Column(name = "request_date")
    Date requestDate;
    @Column(name = "proofreading_done_on")
    Date proofreadingDoneOn;
    @Column(name = "average_rating")
    String averageRating;
    @Column(name = "initiatorComments")
    String initiatorComments;


    @ColumnName(key = "رقم الطلب")
    public Long getId() {
        return Id;
    }

    @ColumnName(key = "جهة الطلب")
    public String getRequestDepartment() {
        return requestDepartment;
    }

    public void setRequestDepartment(String request_Department) {
        this.requestDepartment = request_Department;
    }

    @ColumnName(key = "اسم مقدم الطلب")
    public String getRequestName() {
        return requestName;
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
    public String getWordCount() {
        return wordCount;
    }

    @ColumnName(key = "عدد الصفحات")
    public String getPageCount() {
        return pageCount;
    }

    @ColumnName(key = "حالة الطلب")
    public String getRequestStatus() {
        return requestStatus;
    }

    @ColumnName(key = "المدقق")
    public String getProofreader() {
        return proofreader;
    }

    @ColumnName(key = "تاريخ الطلب")
    public Date getRequestDate() {
        return requestDate;
    }

    @ColumnName(key = "تاريخ التسليم")
    public Date getProofreadingDoneOn() {
        return proofreadingDoneOn;
    }

    @ColumnName(key = "التقييم")
    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    @ColumnName(key = "ملاحظات الجهة الطالبة")
    public String getInitiatorComments() {
        return initiatorComments;
    }
}