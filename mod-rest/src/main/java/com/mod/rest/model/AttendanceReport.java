package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by MinaSamir on 6/16/2020.
 */

@Entity
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

//    @ColumnName(key = "رقم الطلب")
//    public Long getId() {
//        return Id;
//    }

    @ColumnName(order = 1, key = "الجهة")
    public String getDirectorate() {
        return directorate;
    }

    public void setDirectorate(String Directorate) {
        this.directorate = Directorate;
    }

    @ColumnName(order = 2, key = "المرتب")
    public String getTotal() {
        return total;
    }

    @ColumnName(order = 3, key = "الموجود")
    public String getLocated() {
        return located;
    }

    @ColumnName(order = 4, key = "الموجود الفعلي")
    public String getActualLocation() {
        return actualLocation;
    }

    @ColumnName(order = 5, key = "الإلحاق: داخل الوحدة")
    public String getPlacementInsideTheUnit() {
        return placementInsideTheUnit;
    }

    @ColumnName(order = 6, key = "الإلحاق: خارج الوحدة")
    public String getPlacementOutsideTheUnit() {
        return placementOutsideTheUnit;
    }

    @ColumnName(order = 7, key = "الدورات: الداخلية")
    public String getCoursesInterior() {
        return coursesInterior;
    }

    @ColumnName(order = 8, key = "الدورات: الخارجية")
    public String getCoursesExterior() {
        return coursesExterior;
    }

    @ColumnName(order = 9, key = "الإجازات: سنوية")
    public String getVacationAnnual() {
        return vacationAnnual;
    }

    @ColumnName(order = 10, key = "الإجازات: مرضية")
    public String getVacationSick() {
        return vacationSick;
    }

    @ColumnName(order = 11, key = "الإجازات: اضطرارية")
    public String getVacationCasual() {
        return vacationCasual;
    }

    @ColumnName(order = 12, key = "الإجازات: حج")
    public String getVacationPilgrimage() {
        return vacationPilgrimage;
    }

    @ColumnName(order = 13, key = "الإجازات: وضع/أمومة")
    public String getVacationMaternity() {
        return vacationMaternity;
    }

    @ColumnName(order = 14, key = "الإجازات: العدة")
    public String getVacationSeveral() {
        return vacationSeveral;
    }

    @ColumnName(order = 15, key = "الإجازات: مرافق عائلي")
    public String getVacationFamilyFacilities() {
        return vacationFamilyFacilities;
    }

    @ColumnName(order = 16, key = "الإجازات: إجازة بدون راتب")
    public String getVacationUnpaid() {
        return vacationUnpaid;
    }

    @ColumnName(order = 17, key = "الإجازات: دراسية")
    public String getVacationStudy() {
        return vacationStudy;
    }

    @ColumnName(order = 18, key = "الإجازات: ميدانية/إدارية")
    public String getVacationField() {
        return vacationField;
    }

    @ColumnName(order = 19, key = "الإجازات: خاصة")
    public String getVacationPrivate() {
        return vacationPrivate;
    }

    @ColumnName(order = 20, key = "الإجازات: دخول المستشفى")
    public String getVacationHospital() {
        return vacationHospital;
    }

    @ColumnName(order = 21, key = "الإجازات: مرافق علاج")
    public String getVacationTreatmentFacilities() {
        return vacationTreatmentFacilities;
    }

    @ColumnName(order = 22, key = "مراجعة طبية")
    public String getMedicalReview() {
        return medicalReview;
    }

    @ColumnName(order = 23, key = "الغياب")
    public String getAbsence() {
        return absence;
    }

    @ColumnName(order = 24, key = "المناوبون")
    public String getAlternates() {
        return alternates;
    }

    @ColumnName(order = 25, key = "المهام: داخلية")
    public String getTasksInternal() {
        return tasksInternal;
    }

    @ColumnName(order = 26, key = "المهام: خارجية")
    public String getTasksExternal() {
        return tasksExternal;
    }

    @ColumnName(order = 27, key = "إجمالي الموظفين خارج العمل")
    public String getTotalStaffOutOfWork() {
        return totalStaffOutOfWork;
    }

}