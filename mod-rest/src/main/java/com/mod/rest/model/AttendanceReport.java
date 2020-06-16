package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by MinaSamir on 6/16/2020.
 */

@Entity
@Immutable
public class AttendanceReport {
    @Id
    Long Id;
    @Column(name = "Directorate")
    String directorate;
    @Column(name = "total")
    String total;
    @Column(name = "Located")
    String located;
    @Column(name = "actual_location")
    String actualLocation;
    @Column(name = "placement_Inside_the_unit")
    String placementInsideTheUnit;
    @Column(name = "placement_outside_the_unit")
    String placementOutsideTheUnit;
    @Column(name = "courses_Interior")
    String coursesInterior;
    @Column(name = "courses_exterior")
    String coursesExterior;
    @Column(name = "vacation_annual")
    String vacationAnnual;
    @Column(name = "vacation_sick")
    String vacationSick;
    @Column(name = "vacation_casual")
    String vacationCasual;
    @Column(name = "vacation_pilgrimage")
    String vacationPilgrimage;
    @Column(name = "vacation_maternity")
    String vacationMaternity;
    @Column(name = "vacation_several")
    String vacationSeveral;
    @Column(name = "vacation_family_facilities")
    String vacationFamilyFacilities;
    @Column(name = "vacation_unpaid")
    String vacationUnpaid;
    @Column(name = "vacation_study")
    String vacationStudy;
    @Column(name = "vacation_field")
    String vacationField;
    @Column(name = "vacation_private")
    String vacationPrivate;
    @Column(name = "vacation_hospital")
    String vacationHospital;
    @Column(name = "vacation_treatment_facilities")
    String vacationTreatmentFacilities;
    @Column(name = "medical_review")
    String medicalReview;
    @Column(name = "Absence")
    String absence;
    @Column(name = "Alternates")
    String alternates;
    @Column(name = "tasks_internal")
    String tasksInternal;
    @Column(name = "tasks_external")
    String tasksExternal;
    @Column(name = "total_staff_out_of_work")
    String totalStaffOutOfWork;

    @ColumnName(key = "رقم الطلب")
    public Long getId() {
        return Id;
    }

    @ColumnName(key = "الجهة")
    public String getDirectorate() {
        return directorate;
    }

    public void setDirectorate(String Directorate) {
        this.directorate = Directorate;
    }

    @ColumnName(key = "المرتب")
    public String getTotal() {
        return total;
    }

    @ColumnName(key = "الموجود")
    public String getLocated() {
        return located;
    }

    @ColumnName(key = "الموجود الفعلي")
    public String getActualLocation() {
        return actualLocation;
    }

    @ColumnName(key = "الإلحاق: داخل الوحدة")
    public String getPlacementInsideTheUnit() {
        return placementInsideTheUnit;
    }

    @ColumnName(key = "الإلحاق: خارج الوحدة")
    public String getPlacementOutsideTheUnit() {
        return placementOutsideTheUnit;
    }

    @ColumnName(key = "الدورات: الداخلية")
    public String getCoursesInterior() {
        return coursesInterior;
    }

    @ColumnName(key = "الدورات: الخارجية")
    public String getCoursesExterior() {
        return coursesExterior;
    }

    @ColumnName(key = "الإجازات: سنوية")
    public String getVacationAnnual() {
        return vacationAnnual;
    }

    @ColumnName(key = "الإجازات: مرضية")
    public String getVacationSick() {
        return vacationSick;
    }

    @ColumnName(key = "الإجازات: اضطرارية")
    public String getVacationCasual() {
        return vacationCasual;
    }

    @ColumnName(key = "الإجازات: حج")
    public String getVacationPilgrimage() {
        return vacationPilgrimage;
    }

    @ColumnName(key = "الإجازات: وضع/أمومة")
    public String getVacationMaternity() {
        return vacationMaternity;
    }

    @ColumnName(key = "الإجازات: العدة")
    public String getVacationSeveral() {
        return vacationSeveral;
    }

    @ColumnName(key = "الإجازات: مرافق عائلي")
    public String getVacationFamilyFacilities() {
        return vacationFamilyFacilities;
    }

    @ColumnName(key = "الإجازات: إجازة بدون راتب")
    public String getVacationUnpaid() {
        return vacationUnpaid;
    }

    @ColumnName(key = "الإجازات: دراسية")
    public String getVacationStudy() {
        return vacationStudy;
    }

    @ColumnName(key = "الإجازات: ميدانية/إدارية")
    public String getVacationField() {
        return vacationField;
    }

    @ColumnName(key = "الإجازات: خاصة")
    public String getVacationPrivate() {
        return vacationPrivate;
    }

    @ColumnName(key = "الإجازات: دخول المستشفى")
    public String getVacationHospital() {
        return vacationHospital;
    }

    @ColumnName(key = "الإجازات: مرافق علاج")
    public String getVacationTreatmentFacilities() {
        return vacationTreatmentFacilities;
    }

    @ColumnName(key = "مراجعة طبية")
    public String getMedicalReview() {
        return medicalReview;
    }

    @ColumnName(key = "الغياب")
    public String getAbsence() {
        return absence;
    }

    @ColumnName(key = "المناوبون")
    public String getAlternates() {
        return alternates;
    }

    @ColumnName(key = "المهام: داخلية")
    public String getTasksInternal() {
        return tasksInternal;
    }

    @ColumnName(key = "المهام: خارجية")
    public String getTasksExternal() {
        return tasksExternal;
    }

    @ColumnName(key = "إجمالي الموظفين خارج العمل")
    public String getTotalStaffOutOfWork() {
        return totalStaffOutOfWork;
    }

}