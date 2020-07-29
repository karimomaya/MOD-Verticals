package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Data
@Table(name = "O2MyCompanyInstitutionalSupportISMOD_IS_entity_cars_record")
public class CarGeneralReport {
    @Id
    Long Id;
    @Column(name = "type")
    String type;
    @Column (name = "category")
    String category;
    @Column (name = "color")
    String color;
    @Column (name = "model")
    String model;
    @Column (name = "status")
    String status;
    @Column (name = "expiry_date_of_the_license")
    Date expiryDateOfTheLicense;

    @ColumnName(key = "تصنيف السيارة")
    public String getType(){return Utils.removeNullValue(type);}

    @ColumnName(key = "نوع السيارة")
    public String getCategory(){return Utils.removeNullValue(category);}

    @ColumnName(key = "لون السيارة")
    public String getColor(){return Utils.removeNullValue(color);}

    @ColumnName(key = "الموديل")
    public String getModel(){return Utils.removeNullValue(model);}

    @ColumnName(key = "الحالة")
    public String getStatus(){return Utils.removeNullValue(status);}
}
