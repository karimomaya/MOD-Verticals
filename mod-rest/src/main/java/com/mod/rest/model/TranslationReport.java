package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by aly.aboulgheit on 6/25/2020.
 */

@Entity
@Immutable
public class TranslationReport {
    @Id
    Long Id;
    @Column(name="request_name")
    String requestName;
    @Column(name="UnitName")
    String unitName;
    @Column(name="translation_Description")
    String translationDescription;
    @Column(name="translation_Degree_of_confidentiality")
    String translationDegreeOfConfidentiality;
    @Column(name="translation_Degree_of_precedence")
    String translationDegreeOfPrecedence;
    @Column(name="request_date")
    Date requestDate;
    @Column(name="translation_averageEvaluation")
    String translationAverageEvaluation;
    @Column(name="translation_Direction")
    String translationDirection;
    @Column(name="translation_End_Date")
    String translationEndDate;
    @Column(name="translation_evaluationComment")
    String translationEvaluationComment;
    @Column(name="translation_translators")
    String translationTranslators;
    @Column(name="translation_proofreaders")
    String translationProofreaders;
    @Column(name="translation_words_counts")
    String translationWordsCounts;
    @Column(name="request_status")
    String requestStatus;
    @Column(name="translation_pages_counts")
    String translationPagesCounts;



    @ColumnName(key = "اسم مقدم")
    public String getRequestName(){
        return Utils.removeNullValue(requestName);
    }

    @ColumnName(key = "جهة الطلب")
    public String getUnitName(){
        return Utils.removeNullValue(unitName);
    }

    @ColumnName(key = "الموضوع")
    public String getTranslationDescription(){
        return Utils.removeNullValue(translationDescription);
    }

    @ColumnName(key = "درجة السرية")
    public String getTranslationDegreeOfConfidentiality(){
        return Utils.removeNullValue(translationDegreeOfConfidentiality);
    }

    public void setTranslationDegreeOfConfidentiality(String translationDegreeOfConfidentiality){
        this.translationDegreeOfConfidentiality = translationDegreeOfConfidentiality;
    }

    @ColumnName(key = "درجة الأسبقية")
    public String getTranslationDegreeOfPrecedence(){
        return Utils.removeNullValue(translationDegreeOfPrecedence);
    }

    public void setTranslationDegreeOfPrecedence(String translationDegreeOfPrecedence){
        this.translationDegreeOfPrecedence = translationDegreeOfPrecedence;
    }

    @ColumnName(key = "تاريخ الطلب")
    public Date getRequestDate(){
        return requestDate;
    }

    @ColumnName(key = "التقييم")
    public String getTranslationAverageEvaluation(){
        if (translationAverageEvaluation != null){
            //setTranslationAverageEvaluation("لم يتم التقييم");

            return "لم يتم التقييم";
         }else{
            return Utils.removeNullValue(translationAverageEvaluation);
        }

    }

    public void setTranslationAverageEvaluation(String translationAverageEvaluation){

        this.translationAverageEvaluation = translationAverageEvaluation;

    }

    @ColumnName(key = "اتجاه الترجمة")
    public String getTranslationDirection(){
        return Utils.removeNullValue(translationDirection);
    }

    public void setTranslationDirection(String translationDirection){
        this.translationDirection = translationDirection;
    }

    @ColumnName(key = "تاريخ التسليم")
    public String getTranslationEndDate(){
        return translationEndDate;
    }

    @ColumnName(key = "ملاحظات التقييم")
    public String getTranslationEvaluationComment(){
        return Utils.removeNullValue(translationEvaluationComment);
    }

    @ColumnName(key = "المترجم")
    public String getTranslationTranslators(){
        return Utils.removeNullValue(translationTranslators);
    }

    @ColumnName(key = "المدقق")
    public String getTranslationProofreaders(){
        return Utils.removeNullValue(translationProofreaders);
    }

    @ColumnName(key = "عدد الكلمات")
    public String getTranslationWordsCounts(){
        return Utils.removeNullValue(translationWordsCounts);
    }

    @ColumnName(key = "عدد الصفحات")
    public String getTranslationPagesCounts(){
        return Utils.removeNullValue(translationPagesCounts);
    }

    @ColumnName(key = "حالة الطلب")
    public String getRequestStatus(){
        return Utils.removeNullValue(requestStatus);
    }




}