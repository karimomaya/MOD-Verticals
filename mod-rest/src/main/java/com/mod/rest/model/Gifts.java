package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.system.Utils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@Table(name = "O2MyCompanyMinisterofStateforDefenseAffairsMSMMOD_MSM_entity_giftsRecord")
public class Gifts {
    @Id
    Long Id;
    @Column(name = "purchase_date")
    Date purchaseDate;
    @Column(name = "giftName")
    String  giftName;
    @Column (name = "number_available")
    String numberAvailable;
    @Column (name = "expiration_date")
    String expirationDate;

    @ColumnName(key = "نوع الهدية")
     public String getGiftName(){return Utils.removeNullValue(giftName);}
    @ColumnName(key = "العدد المتوفر")
    public String getNumberAvailable(){return Utils.removeNullValue(numberAvailable);}
    @ColumnName(key = "تاريخ الشراء")
    public String getPurshaseDate() {
        if(purchaseDate == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(purchaseDate);
    }
    @ColumnName(key = "تاريخ الإنتهاء")
    public String getExpirationDate(){return Utils.removeNullValue(expirationDate);}
}
