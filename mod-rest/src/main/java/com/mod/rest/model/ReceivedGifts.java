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
@Table(name = "O2MyCompanyMinisterofStateforDefenseAffairsMSMMOD_MSM_entity_receivedGiftsRecord")
public class ReceivedGifts {
    @Id
    Long Id;
    @Column(name = "The_date_the_gift_was_received")
    Date theDateTheGiftWasReceived;
    @Column(name = "gift_Type")
    String  giftName;
    @Column (name = "who_give_the_gift")
    String whoGiveTheGift;
    @Column (name = "ar_value")
    String country;
    @Column (name = "Receiver_name")
    String receiverName;
    @Column (name = "Reason_for_receiving_the_gift")
    String ReasonForReceivingTheGift;

    @ColumnName(order = 1, key = "نوع الهدية")
    public String getGiftName(){return Utils.removeNullValue(giftName);}
    @ColumnName(order = 2, key = "الجهة التي قدمت الهدية")
    public String getWhoGiveTheGift(){return Utils.removeNullValue(whoGiveTheGift);}
    @ColumnName(order = 3, key = "تاريخ الإستلام")
    public String getلإheDateTheGiftWasReceived() {
        if(theDateTheGiftWasReceived == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(theDateTheGiftWasReceived);
    }
    @ColumnName(order = 4, key = "الدولة")
    public String getCountry(){return Utils.removeNullValue(country);}
    @ColumnName(order = 5, key = "الجهة التي استلمت الهدية")
    public String getReceiverName(){return Utils.removeNullValue(receiverName);}
    @ColumnName(order = 6, key = "سبب استلام الهدية")
    public String getReasonForReceivingTheGift(){return Utils.removeNullValue(ReasonForReceivingTheGift);}
}
