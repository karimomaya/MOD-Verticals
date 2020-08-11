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
public class SpentGift {
    @javax.persistence.Id
    Long Id;
    @Column(name = "Date_of_presenting_the_gift")
    Date purchaseDate;

    @Column(name = "reciver_name")
    String  receiverName;

    @Column(name = "The_gift_given")
    String  giftName;

    @Column (name = "ar_value")
    String country;

    @Column (name = "receiver_position")
    String receiverPosition;

    @ColumnName(key = "الجهة التي استلمت الهدية")
    public String getReceiverName(){return Utils.removeNullValue(receiverName);}

    @ColumnName(key = "نوع الهدية")
    public String getGiftName(){return Utils.removeNullValue(giftName);}

    @ColumnName(key = "الدولة")
    public String getCountry(){return Utils.removeNullValue(country);}

    @ColumnName(key = "تاريخ تقديم الهدية")
    public String getPurshaseDate() {
        if(purchaseDate == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(purchaseDate);
    }

    @ColumnName(key = "منصب المستلم")
    public String getExpirationDate(){return Utils.removeNullValue(receiverPosition);}
}

